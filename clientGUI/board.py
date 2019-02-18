import Tkinter as tk


class Board(tk.Frame):
    def __init__(self, parent, tiles, tile_size=20):
        """create a board. input - parent, tilemap(list that shows tiles), and the size of each tile."""
        self.tiles = tiles
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

    def _refresh(self, event=None):
        """take care of the size of the canvas changing"""
        if event: # meaning the size of the board has been changed
            xsize = int((event.width - 1) / len(self.tiles))
            ysize = int((event.height - 1) / len(self.tiles))
            # the game board is square, so we want to see what the biggest square we can make is
            self.tile_size = min(xsize, ysize)
        # erase all of the tiles, so we can draw new ones
        self.canvas.delete('tile')
        # to keep tracck of all of the tiles
        self.canvas_tiles = []
        for i, row in enumerate(self.tiles):
            self.canvas_tiles.append([])
            for j, tile in enumerate(row):
                # print i, j, tile
                # get the locations of the corners of the tiles
                y1 = i * self.tile_size
                y2 = i * self.tile_size + self.tile_size
                x1 = j * self.tile_size
                x2 = j * self.tile_size + self.tile_size
                # color the tile according to what kind it is
                rect = self.canvas.create_rectangle(x1, y1, x2, y2, fill='blue' if tile == 'S' else 'brown', tag='tile',
                                                    activefill='purple')
                self.canvas_tiles[i].append(rect)

    def _click(self, event):
        """take care of a click on a screen"""
        # print 'c', event
        self.canvas.itemconfig('current', fill='silver')
        return

    def color_tile(self, x, y, color="yellow"):
        self._refresh()
        self.canvas.itemconfig(self.canvas_tiles[x][y], fill=color)
        self.canvas.update()
        self.update_idletasks()

