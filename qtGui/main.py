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
button.clicked.connect(lambda x: print('ok'))
layout.addWidget(button)

window.setLayout(layout)
window.show()
# app.exec_()
with loop:
    loop.run_until_complete(main(loop, player))
