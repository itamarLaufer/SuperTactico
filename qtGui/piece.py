from PySide2 import QtCore, QtGui, QtWidgets


class Piece(QtWidgets.QGraphicsPixmapItem):
    path = r'..\res\pics\pieces\piece{}.png'

    def __init__(self, piece_info, team, is_player, messages, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # setup
        self.rect_size = 30
        self.team = team
        self.is_player = is_player
        if self.is_player:
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

        self.setToolTip(self.__str__())
        self.setScale(0.1)
        self.scale()  # make piece fit on a square
        self.mini = self.pixmap().scaled(20, 20)

        self.timer = QtCore.QTimeLine(1000)
        self.timer.setFrameRange(0, 100)
        self.animation = QtWidgets.QGraphicsItemAnimation()
        self.animation.setItem(self)
        self.animation.setTimeLine(self.timer)

    def mousePressEvent(self, event):
        if self.is_player:
            self.messages.put_nowait(['3', str(self.id)])

    def mouseMoveEvent(self, event):
        """Take care of drag and drop"""
        # put necessary data unto dnd item
        if self.is_player:
            data = QtCore.QMimeData()
            data.setProperty('piece', self)
            # setup the drag event
            drag = QtGui.QDrag(event.widget())
            drag.setPixmap(self.mini)
            drag.setHotSpot(QtCore.QPoint(10, 10))
            drag.setMimeData(data)
            drag.start()

    def animate(self, x, y):
        self.animation.clear()
        piece_x, piece_y = self.pos().x(), self.pos().y()
        frames = 200
        for frame in range(1, frames - 1):
            self.animation.setPosAt(frame / frames, QtCore.QPointF(piece_x + ((x - piece_x) * frame / frames),
                                                                   piece_y + ((y - piece_y) * frame / frames)))
        self.animation.setPosAt((frames - 1) / frames, QtCore.QPointF(x, y))
        self.timer.start()

    def add_load(self, piece):
        self.loads.append(piece)

    def empty_load(self):
        self.loads.clear()

    def __str__(self):
        return ' '.join(
            [
                'Type:', str(self.type_id) if self.is_player else '?',
                'Carrying:', str(self.loads),
                'Y:', str(self.y),
                'X:', str(self.x),
                'Personal ID:', str(self.id),
                'Team:', self.team
            ]
        )
