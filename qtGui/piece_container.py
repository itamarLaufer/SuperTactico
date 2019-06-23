from piece import Piece


class PieceContainer:
    """An object to contain piece objects. Will be more useful later on"""

    def __init__(self, messages, team, pieces=None):
        self.messages = messages
        self.pieces = {}
        self.team = team
        if pieces:
            for piece in pieces:
                current = Piece(piece, self.team.player, True, self.messages)
                self.pieces[current.id] = current

    def __iter__(self):
        return self.pieces.values().__iter__()

    def __getitem__(self, item):
        return self.pieces[item]

    def add(self, pieces):
        for piece in pieces:
            current = Piece(piece, self.team.enemy, False, self.messages)
            self.pieces[current.id] = current

    def update(self, new_ids):
        """The ids are updated by the server, so we need to update as well"""
        # for i in range(len(self.pieces)):
        #     self.pieces[i] = new_ids[i]
        new = {}
        for i, id in enumerate(new_ids):
            new[id] = self.pieces[i]
            new[id].id = id
        self.pieces = new

