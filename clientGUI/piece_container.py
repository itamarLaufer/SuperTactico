from piece import Piece


class PieceContainer:
    """An object to contain piece objects. Will be more useful later on"""

    def __init__(self, pieces):
        self.pieces = []
        for piece in pieces:
            self.pieces.append(Piece(piece))

    def __iter__(self):
        return self.pieces.__iter__()

    def __getitem__(self, item):
        return self.pieces[item]

    def update(self, new_ids):
        """The ids are updated by the server, so we need to update as well"""
        for i in range(len(self.pieces)):
            self.pieces[i] = new_ids[i]
