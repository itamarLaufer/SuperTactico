import PySide2.QtCore as QtCore
import PySide2.QtWidgets as QtWidgets
import PySide2.QtGui as QtGui


class Piece(QtWidgets.QGraphicsPixmapItem):
    path = r'..\res\pics\pieces\piece{}.png'

    def __init__(self, piece_info, team, messages, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # setup
        self.rect_size = 30
        self.team = team
        if team == 'b':
            self.type_id = piece_info['typeId']
            self.image_path = self.path.format(self.team + str(self.type_id))
        else:
            self.image_path = self.path.format(self.team)
        self.loads = piece_info['loads']
        self.y, self.x = piece_info['location']
        self.id = piece_info['id']
        self.setPixmap(QtGui.QPixmap(self.image_path))
        self.setPos(self.x * self.rect_size, self.y * self.rect_size)
        self.messages = messages
        
        self.setToolTip("Click and drag this to another square!")
        self.setScale(0.1)
        self.scale()  # make piece fit on a square
        self.mini = self.pixmap().scaled(20, 20)

    def mousePressEvent(self, event):
        if self.team == 'b':
            self.messages.put_nowait(['3', str(self.id)])

    def mouseMoveEvent(self, event):
        """Take care of drag and drop"""
        # put necessary data unto dnd item
        if self.team == 'b':
            data = QtCore.QMimeData()
            data.setProperty('piece', self)
            # setup the drag event
            drag = QtGui.QDrag(event.widget())
            drag.setPixmap(self.mini)
            drag.setHotSpot(QtCore.QPoint(10, 10))
            drag.setMimeData(data)
            drag.start()
