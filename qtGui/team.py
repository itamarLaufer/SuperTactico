class Team:
    """A convenience object to make it easier to pick a team"""
    def __init__(self, team):
        self.player = team
        self.enemy = 'r' if self.player == 'b' else 'r'
