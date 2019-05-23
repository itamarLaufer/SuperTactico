import PySide2.QtCore as QtCore
import PySide2.QtWidgets as QtWidgets
import PySide2.QtGui as QtGui
import piece
import tile


class Board(QtWidgets.QGraphicsView):
    land_color = QtGui.QColor('#8B6508')

    def __init__(self, tiles, pieces, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.tiles = tiles
        self.board = []
        self.rect_size = 30
        self.rect_row = 20
        self.rect_col = 20
        self.scene = QtWidgets.QGraphicsScene()
        self.scaled = 0
        self.scales = []

        # setup all of the tiles
        for i, row in enumerate(self.tiles):
            for j, color in enumerate(row):
                x = j * self.rect_size
                y = i * self.rect_size
                rect = QtCore.QRect(x, y, self.rect_size, self.rect_size)
                self.tiles[i][j] = tile.Tile(color, rect)
                self.scene.addItem(self.tiles[i][j])
        for i in pieces:
            new = piece.Piece(i)
            self.scene.addItem(new)
        self.setScene(self.scene)
        self.setHorizontalScrollBarPolicy(QtCore.Qt.ScrollBarAlwaysOff)
        self.setVerticalScrollBarPolicy(QtCore.Qt.ScrollBarAlwaysOff)
        self.setMinimumSize(self.rect_size * self.rect_row, self.rect_size * self.rect_col)

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
            super().mousePressEvent(event)

    def mouseReleaseEvent(self, event):
        self.setDragMode(QtWidgets.QGraphicsView.NoDrag)
        super().mouseReleaseEvent(event)
