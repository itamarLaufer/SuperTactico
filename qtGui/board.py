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

        brush = QtGui.QBrush(self.land_color, bs=QtCore.Qt.SolidPattern)
        brush.setColor(self.land_color)
        brush.setStyle(QtCore.Qt.SolidPattern)
        # setup all of the tiles
        for i in range(self.rect_row):
            for j in range(self.rect_col):
                rect = QtCore.QRect(i * self.rect_size, j * self.rect_size, self.rect_size, self.rect_size)
                new = tile.Tile(rect)
                new.setPen(QtGui.QPen('black'))
                new.setBrush(brush)
                self.a = self.scene.addItem(new)
        # add some pictures for demonstrating dnd and zoom
        pic = QtGui.QPixmap(
            r'C:\gvahim\mythings\guistuff\Games\SuperTactico\SuperTactico\res\pics\pieces\pieceb5.png')
        self.b = piece.Piece(pic)
        pic = QtGui.QPixmap(
            r'C:\gvahim\mythings\guistuff\Games\SuperTactico\SuperTactico\res\pics\pieces\pieceb9.png')
        self.c = piece.Piece(pic)
        self.c.setPos(30, 0)
        self.scene.addItem(self.b)
        self.scene.addItem(self.c)
        # self.scene.addPixmap(pic)
        self.setScene(self.scene)
        self.setMinimumSize(self.rect_size * self.rect_row + 3, self.rect_size * self.rect_col + 3)

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
