class Piece:
    """An object to represent a piece in the game"""
    path = r'..\res\pics\pieces\piece{}.png'

    def __init__(self, info, team):
        if 'typeId' in info:
            self.type_id = info['typeId']
            self.image_path = self.path.format(team + str(self.type_id))
        else:
            self.image_path = self.path.format(team)
        self.loads = info['loads']
        self.y, self.x = info['location']
        self.id = info['id']
        self.team = team

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
                'Type:', str(self.type_id) if self.team == 'g' else '?',
                'Carrying:', str(self.loads),
                'Y:', str(self.y),
                'X:', str(self.x),
                'Personal ID:', str(self.id),
                'Team:', self.team
            ]
        )
