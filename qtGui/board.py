import PySide2.QtCore as QtCore
import PySide2.QtWidgets as QtWidgets
import PySide2.QtGui as QtGui
import tile
from typing import List, Dict
import time
import piece_container


class Board(QtWidgets.QGraphicsView):
    UPDATE = '4'
    MOVES = '5'
    GAME = '6'

    def __init__(self, tiles: List, pieces: Dict, client, *args, **kwargs):
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
        self.player_pieces = piece_container.PieceContainer(client.todo, pieces=pieces)
        self.enemy_pieces = piece_container.PieceContainer(client.todo)
        self.animation = None
        # setup all of the tiles
        for i, row in enumerate(self.tiles):
            for j, color in enumerate(row):
                x = j * self.rect_size
                y = i * self.rect_size
                rect = QtCore.QRect(x, y, self.rect_size, self.rect_size)
                self.tiles[i][j] = tile.Tile(color, client.todo, rect)
                self.scene.addItem(self.tiles[i][j])
        for i in self.player_pieces:
            self.scene.addItem(i)
        self.setScene(self.scene)
        self.setHorizontalScrollBarPolicy(QtCore.Qt.ScrollBarAlwaysOff)
        self.setVerticalScrollBarPolicy(QtCore.Qt.ScrollBarAlwaysOff)
        self.setMinimumSize(self.rect_size * self.rect_row, self.rect_size * self.rect_col)
        self.timer = QtCore.QTimer()
        self.timer.setInterval(500)
        self.timer.timeout.connect(self.communicate)
        self.timer.start()

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
            else:
                cur_piece = self.enemy_pieces[pid]
            piece_x, piece_y = cur_piece.pos().x(), cur_piece.pos().y()
            if piece_x != x or piece_y != y:
                timer = QtCore.QTimeLine(1000)
                timer.setFrameRange(0, 100)

                self.animation = QtWidgets.QGraphicsItemAnimation()
                self.animation.setItem(cur_piece)
                self.animation.setTimeLine(timer)
                frames = 200
                for i in range(1, frames - 1):
                    self.animation.setPosAt(i / frames, QtCore.QPointF(piece_x + ((x - piece_x) * i / frames),
                                                                       piece_y + ((y - piece_y) * i / frames)))
                self.animation.setPosAt(frames - 1 / frames, QtCore.QPointF(x, y))
                timer.start()

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
                        changed_pieces = order[1]['pieces']
                        self.piece_move(changed_pieces)
                    else:
                        self.enemy_pieces.add(changed_pieces)
                        for i in self.enemy_pieces:
                            self.scene.addItem(i)
                        self.player_pieces.update(order[1]['newIds'])
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
