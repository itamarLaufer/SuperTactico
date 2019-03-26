import board
import Tkinter as tk
import client
import logging
import subprocess
from piece_container import PieceContainer

try:
    # run the server
    server = subprocess.Popen(r'java -jar ..\stEngine\stEngine.jar')
    # create a client to talk to server
    player = client.Client()
    # connect to the server, and get the info needed to start the game
    tiles, pieces = player.connect()
    pieces = PieceContainer(pieces)
    # create a game board
    game_window = tk.Tk()
    game = board.Board(game_window, tiles, pieces)
    game.pack(side="left", fill="both", expand="true", padx=4, pady=4)
    player.mainloop()
    game.mainloop()
except Exception, e:
    logging.exception(e)
finally:
    # end the things that are still running
    player.end()
    server.kill()
