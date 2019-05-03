class Piece:
    """An object to represent a piece in the game"""
    def __init__(self, info, team):
        if team == 'g':
            self.type_id = info['typeId']
        self.loads = info['loads']
        self.y, self.x = info['location']
        self.id = info['id']
        self.team = team
        if team == 'g':
            self.image_path = r'res\pics\pieces\pieceb{}.png'.format(self.type_id)
        else:
            self.image_path = r'res\pics\pieces\piecew.png'

    def add_load(self, piece):
        self.loads.append(piece)

    def remove_load(self, piece):
        self.loads.remove(piece)

    def __add__(self, other):
        self.add_load(other)

    def __sub__(self, other):
        self.remove_load(other)

    def __str__(self):
        return ' '.join(
            [
                'Type:', str(self.type_id),
                'Carrying:', str(self.loads),
                'Y:', str(self.y),
                'X:', str(self.x),
                'Personal ID:', str(self.id)
            ]
        )
