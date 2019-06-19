from PySide2 import QtWidgets, QtGui, QtCore


class ListViewDelegate(QtWidgets.QAbstractItemDelegate):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.size = QtCore.QSize(60, 60)

    def paint(self, painter, option, index):
        x, y = option.rect.left(), option.rect.top()
        height, width = self.size.width(), self.size.height()
        bodytext = index.data(QtCore.Qt.DisplayRole)
        pic = index.data(QtCore.Qt.DecorationRole).pixmap(width, height)
        painter.save()
        font = painter.font()
        font.setPixelSize(30)
        painter.setFont(font)
        rect = painter.boundingRect(x + width * 2 / 3, y + height * 2 / 3, 40, 40, 0, bodytext)
        painter.setPen('gray')
        painter.setBrush(QtGui.QColor('gray'))
        # painter.drawRect(rect)
        painter.drawRect(x, y, width, height)
        painter.drawPixmap(QtCore.QPointF(x, y), pic)
        painter.setPen('black')
        painter.drawText(QtCore.QPointF(x + width, y + height), bodytext)
        painter.restore()

    def sizeHint(self, option, index):
        return self.size
