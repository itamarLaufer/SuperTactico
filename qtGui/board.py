import PySide2.QtCore as QtCore
import PySide2.QtWidgets as QtWidgets
import PySide2.QtGui as QtGui
import piece
import tile


class Board(QtWidgets.QGraphicsView):
    land_color = QtGui.QColor('#8B6508')

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.board = []
        self.rect_size = 30
        self.rect_row = 20
        self.rect_col = 20
        self.scene = QtWidgets.QGraphicsScene()
        self.scaled = 0
        self.scales = []

        for i in range(self.rect_row):
            for j in range(self.rect_col):
                self.board.append(QtCore.QRect(i * self.rect_size, j * self.rect_size, self.rect_size, self.rect_size))

        brush = QtGui.QBrush(self.land_color, bs=QtCore.Qt.SolidPattern)
        brush.setColor(self.land_color)
        brush.setStyle(QtCore.Qt.SolidPattern)
        for i in self.board:
            rect = tile.Tile(i)
            rect.setPen(QtGui.QPen('black'))
            rect.setBrush(brush)
            self.a = self.scene.addItem(rect)
        pic = QtGui.QPixmap(
            r'C:\gvahim\mythings\guistuff\Games\SuperTactico\SuperTactico\tkinterGui\res\pics\pieces\pieceb5.png')
        self.b = piece.Piece(pic)
        pic = QtGui.QPixmap(
            r'C:\gvahim\mythings\guistuff\Games\SuperTactico\SuperTactico\tkinterGui\res\pics\pieces\pieceb9.png')
        self.c = piece.Piece(pic)
        self.c.setPos(30, 0)
        self.scene.addItem(self.b)
        self.scene.addItem(self.c)
        # self.scene.addPixmap(pic)
        self.setScene(self.scene)
        self.setMinimumSize(self.rect_size * self.rect_row + 3, self.rect_size * self.rect_col + 3)

    def wheelEvent(self, event):
        dir = event.delta() / 120
        self.centerOn(event.x(), event.y())
        if dir > 0 and self.scaled < 10:
            scalar = 1 + 1 / self.scaled if self.scaled else 1
            self.scales.append(scalar)
            self.scale(scalar, scalar)
            self.scaled += 1
        elif dir < 0 < self.scaled and self.scales:
            scalar = self.scales.pop(0)
            self.scale(1 / scalar, 1 / scalar)
            self.scaled -= 1
        else:
            return
