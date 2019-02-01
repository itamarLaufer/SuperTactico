import Tkinter as tk


class Board(tk.Frame):
    def __init__(self, parent, tiles, tile_size=20):
        self.tiles = tiles
        self.canvas_height, self.canvas_width = len(self.tiles) * tile_size, len(self.tiles[0]) * tile_size
        tk.Frame.__init__(self, parent)
        self.tile_size = tile_size
        self.canvas = tk.Canvas(self, width=self.canvas_width, height=self.canvas_height, background="gray")
        self.canvas.pack(side="top", fill="both", anchor="c", expand=True)

        self.canvas.bind("<Configure>", self.refresh)
        self.canvas.bind("<Button-1>", self.click)

    def refresh(self, event):
        xsize = int((event.width - 1) / len(self.tiles))
        ysize = int((event.height - 1) / len(self.tiles))
        self.tile_size = min(xsize, ysize)
        self.canvas.delete('tile')
        for i, row in enumerate(self.tiles):
            for j, tile in enumerate(row):
                print i, j, tile
                y1 = i * self.tile_size
                y2 = i * self.tile_size + self.tile_size
                x1 = j * self.tile_size
                x2 = j * self.tile_size + self.tile_size
                print x1, y1, x2, y2
                self.canvas.create_rectangle(x1, y1, x2, y2, fill='blue' if tile == 'S' else 'brown', tag='tile')

    def click(self, event):
        print 'c', event
