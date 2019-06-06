from PySide2 import QtCore, QtGui, QtWidgets


class Tile(QtWidgets.QGraphicsRectItem):
    SEA_COLOR = QtGui.QBrush(QtGui.QColor('#00B2EE'), bs=QtCore.Qt.SolidPattern)
    LAND_COLOR = QtGui.QBrush(QtGui.QColor('#8B6508'), bs=QtCore.Qt.SolidPattern)
    MOVE_COLOR = QtGui.QColor('#00FF00')
    ATTACK_COLOR = QtGui.QColor('#FF0000')
    LOAD_COLOR = QtGui.QColor('#0000FF')

    def __init__(self, tile_type: str, messages,*args, **kwargs):
        super().__init__(*args, **kwargs)
        self.setAcceptDrops(True)
        self.tile_type = tile_type
        if tile_type == 'S':
            brush = self.SEA_COLOR
            move_color = self.MOVE_COLOR
            attack_color = self.ATTACK_COLOR
            load_color = self.LOAD_COLOR
        elif tile_type == 'L':
            brush = self.LAND_COLOR
            move_color = self.MOVE_COLOR.darker(300)
            attack_color = self.ATTACK_COLOR.darker(300)
            load_color = self.LOAD_COLOR.darker(300)
        self.setBrush(brush)
        self.DEFAULT_COLOR = brush
        self.tile_color = self.DEFAULT_COLOR.color()
        self.COLOR_DICT = {
            -1: self.DEFAULT_COLOR,
            0: move_color,
            1: attack_color,
            2: load_color
        }
        self.messages = messages
        self.accepting = False
        self.order_id = []

    def dragEnterEvent(self, event):
        """Take care of dnd item entering tile"""
        if event.mimeData().property('piece') and self.accepting:
            color = self.brush().color().darker(200)  # so we can tell what tile we are over
            brush = QtGui.QBrush(color, bs=QtCore.Qt.SolidPattern)
            self.setBrush(brush)
            event.setAccepted(True)
        else:
            event.setAccepted(False)

    def dropEvent(self, event):
        if self.accepting:
            if self.brush().color() == self.COLOR_DICT[0].darker(200):
                point = self.rect().topLeft()
                event.mimeData().property('piece').setPos(point.x(), point.y())  # move the dragged item to this tile
            # self.tile_color = self.brush().color().lighter(200)  # returns tile color to normal
            # self.color()
            self.messages.put_nowait(['2', *self.order_id])

    def dragLeaveEvent(self, event):
        if self.accepting:
            color = self.brush().color().lighter(200)  # returns tile color to normal
            brush = QtGui.QBrush(color, bs=QtCore.Qt.SolidPattern)
            self.setBrush(brush)

    def setColor(self, color):
        self.tile_color = self.COLOR_DICT[color]

    def color(self):
        brush = QtGui.QBrush(self.tile_color, bs=QtCore.Qt.SolidPattern)
        self.setBrush(brush)
