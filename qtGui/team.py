class Team:
    """A convenience object to make it easier to pick a team"""
    RED = 'r'
    BLUE = 'b'

    def __init__(self, team):
        self.player = team
        self.enemy = self.RED if self.player == self.BLUE else self.BLUE
