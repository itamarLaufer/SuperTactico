import board
import Tkinter as tk
import client


try:
    player = client.Client()
    tiles, pieces = player.connect()
    print '\n\n', tiles, '\n\n'
    print pieces[0]
    game = tk.Tk()
    board.Board(game, tiles).pack(side="left", fill="both", expand="true", padx=4, pady=4)
    player.mainloop()
    tk.mainloop()
finally:
    player.end()
