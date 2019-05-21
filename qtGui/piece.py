import PySide2.QtCore as QtCore
import PySide2.QtWidgets as QtWidgets
import PySide2.QtGui as QtGui


class Piece(QtWidgets.QGraphicsPixmapItem):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.setToolTip("Click and drag this to another square!")
        self.setScale(0.1)
        self.scale()
        self.mini = self.pixmap().scaled(20, 20)
        self.cursor = QtGui.QCursor(self.mini)

    def mousePressEvent(self, event):
        pass

    def mouseMoveEvent(self, event):
        data = QtCore.QMimeData()
        data.setProperty('piece', self)
        drag = QtGui.QDrag(event.widget())
        drag.setPixmap(self.mini)
        drag.setMimeData(data)
        drag.start()
