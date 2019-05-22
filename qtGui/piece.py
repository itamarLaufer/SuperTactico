import PySide2.QtCore as QtCore
import PySide2.QtWidgets as QtWidgets
import PySide2.QtGui as QtGui


class Piece(QtWidgets.QGraphicsPixmapItem):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # setup
        self.setToolTip("Click and drag this to another square!")
        self.setScale(0.1)
        self.scale() # make piece fit on a square
        self.mini = self.pixmap().scaled(20, 20)

    def mousePressEvent(self, event):
        """Currently does nothing, need to override the function so mouseMoveEvent gets called"""
        pass

    def mouseMoveEvent(self, event):
        """Take care of drag and drop"""
        # put necessary data unto dnd item
        data = QtCore.QMimeData()
        data.setProperty('piece', self)
        # setup the drag event
        drag = QtGui.QDrag(event.widget())
        drag.setPixmap(self.mini)
        drag.setMimeData(data)
        drag.start()
