import board
import Tkinter as tk
import client
import logging
import subprocess
import threading
from piece_container import PieceContainer

# TODO make constants file?
# message types from server
ERROR = '0'
SETUP_UPDATE = '4'
MOVES = '5'
GAME_UPDATE = '6'
try:
    # create a client to talk to server
    player = client.Client()
    # connect to the server, and get the info needed to start the game
    tiles, player_pieces = player.connect()
    player_pieces = PieceContainer(player_pieces)
    enemy_pieces = PieceContainer()
    # create a game board
    game_window = tk.Tk()
    game = board.Board(game_window, tiles, player_pieces, enemy_pieces)
    game.pack(side="left", fill="both", expand="true", padx=4, pady=4)
    tk.Button(game_window, text="start", command=lambda: player.todo.append(['1', 'john'])).pack()
    who = tk.StringVar()
    lab = tk.Label(game_window, textvariable=who)
    lab.pack()
    pid = tk.StringVar()
    lab2 = tk.Label(game_window, textvariable=pid)
    lab2.pack()
    player.set(True)
    game_window.protocol("WM_DELETE_WINDOW", lambda: player.set(False))
    pmain = threading.Thread(target=player.mainloop)
    pmain.setDaemon(True)
    pmain.start()
    gmain = threading.Thread(target=game.mainloop)
    gmain.setDaemon(True)
    gmain.start()
    turn = True
    while player.not_end:
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
            elif order[0] == SETUP_UPDATE:
                changed_pieces = order[1]['pieces']
                game.turn(changed_pieces)
            elif order[0] == GAME_UPDATE:
                changed_pieces = order[1]['pieces']
                if 'battleResult' in order[1]:
                    enemies = []
                    for piece in changed_pieces:
                        if piece['id'] in enemy_pieces.pieces:
                            game.fight(piece, piece['typeId'])
                if enemy_pieces.pieces:
                    game.turn(changed_pieces)
                else:
                    enemy_pieces.add(changed_pieces)
                    game.add_enemies()
                    player_pieces.update(order[1]['newIds'])
                who.set(order[1]['turn'])
                turn = order[1]['turn'] == 1
                if order[1]['turn'] == 0:
                    player.todo.append('')
        # if there are messages to send to the server
        if game.events:
            event = game.events.pop(0)
            if not turn:
                event = None
                continue
            # find out what a piece can do
            if event[0] == "3":
                player.todo.append(event)
                pid.set(event[1])
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
