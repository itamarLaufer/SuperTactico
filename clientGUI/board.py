import Tkinter as tk
from PIL import ImageTk, Image
from itertools import chain


class Board(tk.Frame):
    ORDER_MOVE = 0
    ORDER_ATTACK = 1
    ORDER_LOAD = 2
    ORDER_UNLOAD = 3
    MOVE_COLOR = 'green'
    ATTACK_COLOR = 'red'
    LOAD_COLOR = 'blue'
    UNLOAD_COLOR = 'yellow'
    CLICKED_COLOR = 'orange'
    GROUND = 'darkgoldenrod4'
    SKY = 'deepskyblue2'

    def __init__(self, parent, tiles, pieces, tile_size=30):
        """create a board. input - parent, tilemap(list that shows tiles), and the size of each tile."""
        self.tiles = tiles
        self.pieces = pieces
        self.pieces_dict = {}
        # calculate how tall the canvas needs to be, based on the size of each tile, and the amount of tiles
        self.canvas_height, self.canvas_width = len(self.tiles) * tile_size, len(self.tiles[0]) * tile_size
        tk.Frame.__init__(self, parent)
        self.tile_size = tile_size
        self.canvas = tk.Canvas(self, width=self.canvas_width, height=self.canvas_height, background="gray")
        self.canvas.pack(side="top", fill="both", anchor="c", expand=True)
        # bind the changing of the size of the canvas to a function, so we can update the drawing
        self.canvas_tiles = []
        self.canvas.bind("<Configure>", self._refresh)
        self.canvas.bind("<Button-1>", self._click)
        self.images = []
        self.events = []

    def draw_tiles(self):
        """draw all of the tiles on the board"""
        # erase all of the tiles, so we can draw new ones
        self.canvas.delete('tile')
        # to keep track of all of the tiles
        self.canvas_tiles = []
        for i, row in enumerate(self.tiles):
            self.canvas_tiles.append([])
            for j, tile in enumerate(row):
                # get the locations of the corners of the tiles
                y1 = i * self.tile_size
                y2 = i * self.tile_size + self.tile_size
                x1 = j * self.tile_size
                x2 = j * self.tile_size + self.tile_size
                # color the tile according to what kind it is
                rect = self.canvas.create_rectangle(x1, y1, x2, y2, fill=self.SKY if tile == 'S' else self.GROUND,
                                                    tags='tile',
                                                    activefill='purple')
                self.canvas_tiles[i].append(rect)

    def redraw_tiles(self):
        for row1, row2 in zip(self.tiles, self.canvas_tiles):
            for color, tile in zip(row1, row2):
                self.canvas.itemconfig(tile, fill=self.SKY if color == 'S' else self.GROUND)

    def place_piece(self, piece):
        """place a picture of a piece in the location of the piece"""
        tile_size = self.tile_size
        # if the piece doesn't exist yet, create it
        if piece not in self.pieces_dict.values():
            img = Image.open(piece.image_path)
            img.thumbnail((tile_size, tile_size))
            tkimg = ImageTk.PhotoImage(image=img)
            id = self.canvas.create_image(piece.x * tile_size, piece.y * tile_size, image=tkimg, tags='pic',
                                          anchor='nw')
            self.pieces_dict[id] = piece
            # if we don't save images, then they don't appear (garbage collector)
            self.images.append(tkimg)
        # if the piece exists already, update it
        else:
            for tk_id, pieces in self.pieces_dict.items():
                if pieces == piece:
                    self.canvas.coords(tk_id, (piece.x * tile_size, piece.y * tile_size))
                    self.redraw_tiles()
                    self.canvas.tag_raise('pic', 'tile')
                    break

    def _refresh(self, event=None):
        """take care of the size of the canvas changing"""
        if event:  # meaning the size of the board has been changed
            xsize = int((event.width - 1) / len(self.tiles))
            ysize = int((event.height - 1) / len(self.tiles))
            # the game board is square, so we want to see what the biggest square we can make is
            self.tile_size = min(xsize, ysize)
        # erase all of the pictures, so we can draw new ones
        self.canvas.delete('pic')
        self.draw_tiles()
        self.pieces_dict = {}
        self.images = []
        for piece in self.pieces:
            self.place_piece(piece)

    def _click(self, event):
        """take care of a click on a screen"""
        # find out what was clicked
        current = self.canvas.find_withtag('current')[0]
        # if we clicked a piece
        if current in self.pieces_dict.keys():
            # erase the previous drawings
            self.redraw_tiles()
            self.canvas.tag_raise('pic', 'tile')
            piece = self.pieces_dict[current]
            # if the piece is on a default color (if it isn't, then we're clicking it to complete an action)
            if self.canvas.itemcget(self.canvas_tiles[piece.y][piece.x], "fill") in [self.SKY, self.GROUND]:
                self.color_tile(piece_id=piece.id)
                self.events.append(("3", str(piece.id)))
        # if we clicked a tile
        elif current in list(chain.from_iterable(self.canvas_tiles)):
            # if the tile is not a default color (meaning it is something to do)
            if self.canvas.itemcget(current, "fill") not in [self.SKY, self.GROUND]:
                # get the x and y of the tile, but reverse them because thats the format used by the API
                coordinates = self.canvas.coords(current)[:2][::-1]
                self.events.append(("2", [int(i / self.tile_size) for i in coordinates]))

    def color_id_to_color(self, color_id):
        """get a color based on a colors id"""
        # TODO make into dictionary?
        if color_id == self.ORDER_MOVE:
            color_id = self.MOVE_COLOR
        elif color_id == self.ORDER_ATTACK:
            color_id = self.ATTACK_COLOR
        elif color_id == self.ORDER_LOAD:
            color_id = self.LOAD_COLOR
        elif color_id == self.ORDER_UNLOAD:
            color_id = self.UNLOAD_COLOR
        else:
            color_id = self.CLICKED_COLOR
        return color_id

    def color_tile(self, x=None, y=None, piece_id=None, color_id=None):
        """set the of a tile (by x y coords or by the id of a piece) to the chosen color. default color is orange"""
        color = self.color_id_to_color(color_id)
        # if we do it by id
        if piece_id is not None:
            piece = self.pieces[piece_id]
            self.canvas.itemconfig(self.canvas_tiles[piece.y][piece.x], fill=color)
        # if we do it by coordinates
        elif x is not None and y is not None:
            self.canvas.itemconfig(self.canvas_tiles[x][y], fill=color)
        self.canvas.update()
        self.update_idletasks()
