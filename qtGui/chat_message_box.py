from PySide2 import QtCore, QtGui, QtWidgets
import bubble_draw
from html import escape


class ChatMessagesBox(QtWidgets.QListView):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.mymodel = QtGui.QStandardItemModel()
        self.setResizeMode(QtWidgets.QListView.Adjust)
        self.setWordWrap(True)
        self.setVerticalScrollMode(QtWidgets.QAbstractItemView.ScrollPerPixel)
        self.setVerticalScrollBarPolicy(QtCore.Qt.ScrollBarAlwaysOff)
        self.setHorizontalScrollBarPolicy(QtCore.Qt.ScrollBarAlwaysOff)
        self.setModel(self.mymodel)
        self.setItemDelegate(bubble_draw.ListViewDelegate())
        self.startAutoScroll()

    def add_good(self, text):
        text = escape(text)
        text = text.strip()
        item = QtGui.QStandardItem(text)
        item.setData("Outgoing", QtCore.Qt.UserRole + 1)
        self.mymodel.appendRow(item)
        self.scrollToBottom()

    def add_bad(self, text):
        text = escape(text)
        text = text.strip()
        item = QtGui.QStandardItem(str(text))
        item.setData("Incoming", QtCore.Qt.UserRole + 1)
        self.mymodel.appendRow(item)
        self.scrollToBottom()


if __name__ == '__main__':
    app = QtWidgets.QApplication()

    listview = ChatMessagesBox()
    listview.add_bad("This is item one")
    listview.add_good(
        "This is item two, it is a very long item, but it's not the item's fault, it is me typing all this text.")
    listview.add_bad("This is the third item")

    listview.show()

    app.exec_()
