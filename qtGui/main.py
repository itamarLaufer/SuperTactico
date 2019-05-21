import sys
import PySide2.QtCore as QtCore
import PySide2.QtWidgets as QtWidgets
import PySide2.QtGui as QtGui
import board

app = QtWidgets.QApplication(sys.argv)
game = board.Board()
window = QtWidgets.QWidget()
layout = QtWidgets.QVBoxLayout()
scene = QtWidgets.QGraphicsScene()
layout.addWidget(game)
button = QtWidgets.QPushButton('start')
button.clicked.connect(lambda x: print('ok'))
layout.addWidget(button)
qe = QtWidgets.QLineEdit('')
qe.setDragEnabled(True)
layout.addWidget(qe)
size = QtWidgets.QDesktopWidget().size()
window.setLayout(layout)
window.show()
game.setMinimumSize(game.rect_size * game.rect_row + 3, game.rect_size * game.rect_col + 3)
app.exec_()
