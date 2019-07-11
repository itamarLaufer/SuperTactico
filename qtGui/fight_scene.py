from PySide2 import QtWidgets, QtGui, QtCore


class FightScene(QtWidgets.QGraphicsScene):
    def __init__(self, team, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.team = team
        self.good = None
        self.bad = None
        self.status = None
        self.dead = []
        self.timeLine = QtCore.QTimeLine(1500, self)
        self.timeLine.setFrameRange(0, 19)
        self.timeLine.frameChanged.connect(self.change)
        self.size = 200

        self.good_image = QtWidgets.QGraphicsPixmapItem()
        self.good_image.setPos(self.size, self.size)
        self.addItem(self.good_image)
        self.bad_image = QtWidgets.QGraphicsPixmapItem()
        self.bad_image.setPos(0, 0)
        self.bad_image.setTransformOriginPoint(self.size / 2, self.size / 2)
        self.addItem(self.bad_image)

    def set_good(self, piece, status):
        self.good = piece
        self.status = status

    def set_bad(self, piece):
        self.bad = piece

    def clean(self):
        self.good_image.setPixmap(
            QtGui.QPixmap(r'..\res\pics\pieces\piece{}.png'.format(self.team.player + str(self.good['typeId']))).scaled(
                self.size, self.size))
        self.bad_image.setRotation(180 if 13 <= self.bad['typeId'] <= 18 else 0)
        self.bad_image.setPixmap(
            QtGui.QPixmap(r'..\res\pics\pieces\piece{}.png'.format(self.team.enemy + str(self.bad['typeId']))).scaled(
                self.size, self.size))

    def animate(self):
        self.dead = []
        if self.status == 'VICTORY':
            self.dead.append(self.bad_image)
        if self.status == 'TIE':
            self.dead.append(self.bad_image)
            self.dead.append(self.good_image)
        if self.status == 'DEFEAT':
            self.dead.append(self.good_image)
        QtCore.QTimer.singleShot(300, self.timeLine.start)

    def change(self, a):
        pic = QtGui.QPixmap(r'..\res\pics\explosion\{}.png'.format(a)).scaled(200, 200)
        for i in self.dead:
            i.setPixmap(pic)
