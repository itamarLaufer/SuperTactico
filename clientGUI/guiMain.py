import board
import Tkinter as tk
import client
import logging
import subprocess
import thread
from piece_container import PieceContainer
# TODO make constants file?
# message types from server
ERROR = '0'
UPDATE = '4'
MOVES = '5'
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
    tk.Button(game_window, text='start', command=lambda: thread.start_new_thread(player.mainloop, ())).pack()
    # thread.start_new_thread(player.mainloop, ())
    thread.start_new_thread(game.mainloop, ())
    while True:
        # if there are messages to process from server
        if player.received:
            order = player.received.pop(0)
            # if order is options for piece
            if order[0] == MOVES:
                actor_id = order[1]['actorId']
                locations = order[1]['orders']
                # color all locations of piece options
                for location in locations:
                    game.color_tile(x=location['location'][0], y=location['location'][1], color_id=location['typeId'])
            # if order is location to move pieces to
            elif order[0] == UPDATE:
                changed_pieces = order[1]['pieces']
                for piece in changed_pieces:
                    moved = pieces[piece['id']]
                    location = piece['location']
                    moved.y = location[0]
                    moved.x = location[1]
                    game.place_piece(moved)
        # if there are messages to send to the server
        if game.events:
            event = game.events.pop(0)
            # find out what a piece can do
            if event[0] == "3":
                player.todo.append(event)
            # execute an option
            elif event[0] == "2":
                for i, location in enumerate(locations):
                    if location['location'] == event[1]:
                        player.todo.append([event[0], str(actor_id), str(i)])
except Exception, e:
    logging.exception(e)
finally:
    # end the things that are still running
    player.end()
    server.kill()
