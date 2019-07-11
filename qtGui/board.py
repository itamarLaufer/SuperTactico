from PySide2 import QtCore, QtGui, QtWidgets
import tile
from typing import List, Dict
import time
import piece_container
import fight_scene


class Board(QtWidgets.QGraphicsView):
    UPDATE = '4'
    MOVES = '5'
    GAME = '6'
    CHAT = '8'

    def __init__(self, tiles: List, pieces: Dict, client, chat, team, player_grave, enemy_grave, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.tiles = tiles
        self.changed_tiles = []
        self.board = []
        self.rect_size = 30
        self.rect_row = 20
        self.rect_col = 20
        self.scene = QtWidgets.QGraphicsScene()
        self.scaled = 0
        self.scales = []
        self.client = client
        self.player_grave = player_grave
        self.enemy_grave = enemy_grave
        self.player_pieces = piece_container.PieceContainer(client.todo, team, pieces=pieces)
        self.enemy_pieces = piece_container.PieceContainer(client.todo, team)
        self.animation = None

        self.chat = chat
        # setup all of the tiles
        for i, row in enumerate(self.tiles):
            for j, color in enumerate(row):
                x = j * self.rect_size
                y = i * self.rect_size
                rect = QtCore.QRect(x, y, self.rect_size, self.rect_size)
                self.tiles[i][j] = tile.Tile(color, client.todo, rect)
                self.scene.addItem(self.tiles[i][j])
        for i in self.player_pieces:
            self.player_grave.add_item(i.type_id)
            self.enemy_grave.add_item(i.type_id)
            self.scene.addItem(i)
        self.setScene(self.scene)
        self.setHorizontalScrollBarPolicy(QtCore.Qt.ScrollBarAlwaysOff)
        self.setVerticalScrollBarPolicy(QtCore.Qt.ScrollBarAlwaysOff)
        self.setMinimumSize(self.rect_size * self.rect_row, self.rect_size * self.rect_col)
        self.timer = QtCore.QTimer()
        self.timer.setInterval(500)
        self.timer.timeout.connect(self.communicate)
        self.timer.start()
        self.fight_scene = fight_scene.FightScene(team)
        self.fight_scene.timeLine.stateChanged.connect(lambda state: self.setScene(
            self.scene if state == self.fight_scene.timeLine.State.NotRunning else self.fight_scene))

    def wheelEvent(self, event):
        dir = event.delta()
        self.centerOn(event.x(), event.y())  # make zoom occur around mouse location
        if dir > 0 and self.scaled < 10:
            scalar = 1 + 1 / self.scaled if self.scaled else 1  # calculate zoom so it zooms 10% each time
            self.scales.append(scalar)
            self.scale(scalar, scalar)
            self.scaled += 1
        elif dir < 0 < self.scaled and self.scales:
            scalar = self.scales.pop(0)  # zoom out the same amount the last zoom in was
            self.scale(1 / scalar, 1 / scalar)
            self.scaled -= 1
        else:
            return

    def mousePressEvent(self, event):
        if self.scales:
            self.setDragMode(QtWidgets.QGraphicsView.ScrollHandDrag)
        for i in self.changed_tiles:
            i.accepting = False
        super().mousePressEvent(event)

    def mouseReleaseEvent(self, event):
        if self.scales:
            self.setDragMode(QtWidgets.QGraphicsView.NoDrag)
        super().mouseReleaseEvent(event)

    def piece_move(self, pieces):
        for i in pieces:
            loc = i['location']
            x, y = loc[1] * self.rect_size, loc[0] * self.rect_size
            pid = i['id']
            if pid in self.player_pieces.pieces:
                cur_piece = self.player_pieces[pid]
                grave = self.player_grave
            else:
                cur_piece = self.enemy_pieces[pid]
                grave = self.enemy_grave
            piece_x, piece_y = cur_piece.pos().x(), cur_piece.pos().y()
            if x < 0 and y < 0:
                self.scene.removeItem(cur_piece)
                grave.add_piece(i['typeId'])
            elif piece_x != x or piece_y != y:
                cur_piece.animate(x, y)
            if 'loads' in i.keys():
                cur_piece.empty_load()
                for j in i['loads']:
                    piece_id = j['id']
                    if cur_piece in self.player_pieces:
                        loaded = self.player_pieces[piece_id]
                    elif cur_piece in self.enemy_pieces:
                        loaded = self.enemy_pieces[piece_id]
                    self.scene.removeItem(loaded)
                    cur_piece.add_load(piece_id)

    def fight(self, pieces, result):
        pid = pieces[0]['id']
        if pid in self.player_pieces.pieces:
            self.fight_scene.set_good(pieces[0], result)
            self.fight_scene.set_bad(pieces[1])
        else:
            self.fight_scene.set_good(pieces[1], result)
            self.fight_scene.set_bad(pieces[0])
        self.fight_scene.clean()
        self.setScene(self.fight_scene)
        self.fight_scene.animate()

    def communicate(self):
        if not self.client.received.empty():
            order = self.client.received.get_nowait()
            if order:
                new = []
                if order[0] == self.MOVES:
                    actor_id = order[1]['actorId']
                    locations = order[1]['orders']
                    # color all locations of piece options
                    for i, location in enumerate(locations):
                        x = location['location'][0]
                        y = location['location'][1]
                        cur = self.tiles[x][y]
                        new.append(cur)
                        cur.setColor(location['typeId'])
                        cur.accepting = True
                        cur.color()
                        cur.order_id = [str(actor_id), str(i)]
                elif order[0] == self.UPDATE:
                    changed_pieces = order[1]['pieces']
                    self.piece_move(changed_pieces)
                    # piece.setPos(x, y)
                elif order[0] == self.GAME:
                    changed_pieces = order[1]['pieces']
                    if self.enemy_pieces.pieces:
                        if 'battleResult' in order[1]:
                            self.fight(order[1]['pieces'], order[1]['battleResult'])
                        self.piece_move(changed_pieces)
                    else:
                        self.enemy_pieces.add(changed_pieces)
                        self.enemy_grave.show()
                        for i in self.enemy_pieces:
                            self.scene.addItem(i)
                        self.player_pieces.update(order[1]['newIds'])
                elif order[0] == self.CHAT:
                    self.chat(order[1])
                new = set(new)
                changed_tiles = set(self.changed_tiles)
                intersection = changed_tiles & new
                changed_remainder = changed_tiles - new
                new_remainder = new - changed_tiles
                self.changed_tiles = intersection.union(new_remainder)
                for i in changed_remainder:
                    i.accepting = False
                    i.setColor(-1)
                    i.color()

    def resizeEvent(self, event):
        if event.oldSize().width() > 0:
            old = self.rect_size
            self.rect_size = int(min(event.size().height() / 20, event.size().width() / 20))
            for piece in self.player_pieces.pieces.values():
                pos = piece.pos()
                piece.setPos(pos.x() / old * self.rect_size, pos.y() / old * self.rect_size)
                piece.rect_size = self.rect_size
            for piece in self.enemy_pieces.pieces.values():
                pos = piece.pos()
                piece.setPos(pos.x() / old * self.rect_size, pos.y() / old * self.rect_size)
                piece.rect_size = self.rect_size
            for i, row in enumerate(self.tiles):
                for j, cur_tile in enumerate(row):
                    x = j * self.rect_size
                    y = i * self.rect_size
                    self.tiles[i][j].setRect(x, y, self.rect_size, self.rect_size)
