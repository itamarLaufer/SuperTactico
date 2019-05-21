import PySide2.QtCore as QtCore
import PySide2.QtWidgets as QtWidgets
import PySide2.QtGui as QtGui


class Tile(QtWidgets.QGraphicsRectItem):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.setAcceptDrops(True)

    def dragEnterEvent(self, event):
        if event.mimeData().property('piece'):
            color = self.brush().color().darker(200)
            brush = QtGui.QBrush(color, bs=QtCore.Qt.SolidPattern)
            self.setBrush(brush)
            event.setAccepted(True)
        else:
            event.setAccepted(False)

    def dropEvent(self, event):
        point = self.rect().topLeft()
        event.mimeData().property('piece').setPos(point.x(), point.y())
        color = self.brush().color().lighter(200)
        brush = QtGui.QBrush(color, bs=QtCore.Qt.SolidPattern)
        self.setBrush(brush)

    def dragLeaveEvent(self, event):
        color = self.brush().color().lighter(200)
        brush = QtGui.QBrush(color, bs=QtCore.Qt.SolidPattern)
        self.setBrush(brush)