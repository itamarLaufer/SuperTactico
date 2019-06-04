from PySide2.QtWidgets import *
from PySide2.QtCore import *

app = QApplication()
ball = QGraphicsEllipseItem(0, 0, 20, 20)

timer = QTimeLine(5000)
timer.setFrameRange(0, 100)

animation = QGraphicsItemAnimation()
animation.setItem(ball)
animation.setTimeLine(timer)

for i in range(200):
    animation.setPosAt(i / 200.0, QPointF(i, i))

scene = QGraphicsScene()
scene.setSceneRect(0, 0, 250, 250)
scene.addItem(ball)

view = QGraphicsView(scene)
view.show()

timer.start()

app.exec_()