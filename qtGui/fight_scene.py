from PySide2 import QtWidgets, QtGui, QtCore


class FightScene(QtWidgets.QGraphicsScene):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.good = None
        self.bad = None
        self.status = None
        self.dead = []

    def setGood(self, piece, status):
        self.good = piece
        self.status = status

    def setBad(self, piece):
        self.bad = piece

    def animate(self):
        good = QtWidgets.QGraphicsPixmapItem()
        good.setPixmap(QtGui.QPixmap(r'..\res\pics\pieces\pieceb{}.png'.format(self.bad['typeId'])).scaled(200, 200))
        good.setPos(0, 300)
        self.addItem(good)
        bad = QtWidgets.QGraphicsPixmapItem()
        bad.setRotation(180 if 13 <= self.bad['typeId'] <= 18 else 0)
        bad.setPixmap(QtGui.QPixmap(r'..\res\pics\pieces\piecer{}.png'.format(self.bad['typeId'])).scaled(200, 200))
        self.addItem(bad)
        self.dead = []
        if self.status == 'VICTORY':
            self.dead.append(bad)
        if self.status == 'TIE':
            self.dead.append(bad)
            self.dead.append(good)
        if self.status == 'DEFEAT':
            self.dead.append(good)
        timeLine = QtCore.QTimeLine(1500, self)
        timeLine.setFrameRange(0, 19)
        timeLine.frameChanged.connect(self.change)
        timeLine.start()

    def change(self, a):
        pic = QtGui.QPixmap(r'..\res\pics\explosion\{}c.png'.format(a)).scaled(200, 200)
        for i in self.dead:
            i.setPixmap(pic)
