from PySide2 import QtCore, QtGui, QtWidgets
import piece_draw


class PieceList(QtWidgets.QListView):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.mymodel = QtGui.QStandardItemModel()
        self.setResizeMode(QtWidgets.QListView.Adjust)
        self.setWordWrap(True)
        self.setVerticalScrollMode(QtWidgets.QAbstractItemView.ScrollPerPixel)
        # self.setVerticalScrollBarPolicy(QtCore.Qt.ScrollBarAlwaysOff)
        self.setHorizontalScrollBarPolicy(QtCore.Qt.ScrollBarAlwaysOff)
        self.setModel(self.mymodel)
        self.setItemDelegate(piece_draw.ListViewDelegate())
        self.startAutoScroll()
        # self.resize(60, 180)
        self.index = 0
        self.list = []

    def add_item(self):
        self.list.append(1)
        item = QtGui.QStandardItem(QtGui.QIcon(QtGui.QPixmap(f'..\\res\pics\pieces\pieceb{self.index}.png')),
                                   str(self.list[self.index]))
        self.index += 1
        item.setData("Outgoing", QtCore.Qt.UserRole + 1)
        self.mymodel.appendRow(item)
        # self.scrollToBottom()

    def add_piece(self, index):
        item = self.mymodel.item(index, 0)
        item.setText(str(int(item.text()) + 1))
        self.scrollTo(self.mymodel.index(index, 0))


if __name__ == '__main__':
    app = QtWidgets.QApplication()

    listview = PieceList()
    # listview.add_bad("This is item one")
    for i in range(19):
        listview.add_item()
    # listview.add_bad("This is the third item")
    listview.add_piece(1)
    listview.add_piece(2)
    listview.add_piece(2)
    listview.add_piece(3)
    listview.add_piece(3)
    listview.add_piece(3)
    listview.show()

    app.exec_()
