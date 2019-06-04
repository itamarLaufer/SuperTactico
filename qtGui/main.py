import sys
import PySide2.QtCore as QtCore
import PySide2.QtWidgets as QtWidgets
import PySide2.QtGui as QtGui
import board
import client
import asyncio
from asyncqt import QEventLoop, QThreadExecutor


async def main(loop, player):
    write = loop.create_task(player.send())
    read = loop.create_task(player.receive())
    await write
    await read
    # await player.send()


app = QtWidgets.QApplication(sys.argv)
loop = QEventLoop(app)
asyncio.set_event_loop(loop)

player = client.Client()
tiles, player_pieces = loop.run_until_complete(player.connect())

game = board.Board(tiles, player_pieces, player)

window = QtWidgets.QWidget()
layout = QtWidgets.QVBoxLayout()
scene = QtWidgets.QGraphicsScene()
layout.addWidget(game)
button = QtWidgets.QPushButton('start')
button.clicked.connect(lambda x: player.todo.put_nowait(['1', 'john']))
layout.addWidget(button)

window.setLayout(layout)
if sys.argv[1] == 'r':
    desk = app.desktop()
    x = desk.width() - window.size().width()
    y = 0
    window.move(x, y)
else:
    desk = app.desktop()
    x = 0
    y = 0
    window.move(x, y)
window.show()
# app.exec_()
with loop:
    loop.run_until_complete(main(loop, player))
