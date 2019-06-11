from PySide2 import QtWidgets, QtGui, QtCore


class ListViewDelegate(QtWidgets.QAbstractItemDelegate):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.d_radius = 5
        self.d_toppadding = 5
        self.d_bottompadding = 3
        self.d_leftpadding = 5
        self.d_rightpadding = 5
        self.d_verticalmargin = 15
        self.d_horizontalmargin = 10
        self.d_pointerwidth = 10
        self.d_pointerheight = 17
        self.d_widthfraction = .7

    def paint(self, painter, option, index):
        bodydoc = QtGui.QTextDocument()
        textOption = QtGui.QTextOption(bodydoc.defaultTextOption())
        textOption.setWrapMode(QtGui.QTextOption.WrapAtWordBoundaryOrAnywhere)
        bodydoc.setDefaultTextOption(textOption)
        bodydoc.setDefaultFont(QtGui.QFont("Roboto", 12))
        bodytext = index.data(QtCore.Qt.DisplayRole)
        bodydoc.setHtml(bodytext)

        contentswidth = option.rect.width() * self.d_widthfraction - self.d_horizontalmargin - self.d_pointerwidth - self.d_leftpadding - self.d_rightpadding
        bodydoc.setTextWidth(contentswidth)
        bodyheight = bodydoc.size().height()

        incoming = index.data(QtCore.Qt.UserRole + 1) == "Incoming"

        painter.save()
        painter.setRenderHint(QtGui.QPainter.Antialiasing)

        # painter.drawRect(option.rect)

        painter.translate(option.rect.left() + self.d_horizontalmargin,
                          option.rect.top() + (self.d_verticalmargin if (index.row() == 0) else 0))

        # background color for chat bubble
        bgcolor = QtGui.QColor("#66ff66")
        if incoming:
            bgcolor = "#DDDDDD"

        # create chat bubble
        pointie = QtGui.QPainterPath()

        # left bottom
        pointie.moveTo(0, bodyheight + self.d_toppadding + self.d_bottompadding)

        # right bottom
        pointie.lineTo(
            0 + contentswidth + self.d_pointerwidth + self.d_leftpadding + self.d_rightpadding - self.d_radius,
            bodyheight + self.d_toppadding + self.d_bottompadding)
        pointie.arcTo(
            0 + contentswidth + self.d_pointerwidth + self.d_leftpadding + self.d_rightpadding - 2 * self.d_radius,
            bodyheight + self.d_toppadding + self.d_bottompadding - 2 * self.d_radius,
            2 * self.d_radius, 2 * self.d_radius, 270, 90)

        # right top
        pointie.lineTo(0 + contentswidth + self.d_pointerwidth + self.d_leftpadding + self.d_rightpadding,
                       0 + self.d_radius)
        pointie.arcTo(
            0 + contentswidth + self.d_pointerwidth + self.d_leftpadding + self.d_rightpadding - 2 * self.d_radius, 0,
            2 * self.d_radius, 2 * self.d_radius, 0, 90)

        # left top
        pointie.lineTo(0 + self.d_pointerwidth + self.d_radius, 0)
        pointie.arcTo(0 + self.d_pointerwidth, 0, 2 * self.d_radius, 2 * self.d_radius, 90, 90)

        # left bottom almost (here is the pointie)
        pointie.lineTo(0 + self.d_pointerwidth,
                       bodyheight + self.d_toppadding + self.d_bottompadding - self.d_pointerheight)
        pointie.closeSubpath()

        # rotate bubble for outgoing messages
        if incoming:
            painter.translate(
                option.rect.width() - pointie.boundingRect().width() - self.d_horizontalmargin - self.d_pointerwidth, 0)
            painter.translate(pointie.boundingRect().center())
            painter.rotate(180)
            painter.translate(-pointie.boundingRect().center())

        # now paint it!
        painter.setPen(QtGui.QPen(bgcolor))
        painter.drawPath(pointie)
        painter.fillPath(pointie, QtGui.QBrush(bgcolor))

        # rotate back or painter is going to paint the text rotated...
        if incoming:
            painter.translate(pointie.boundingRect().center())
            painter.rotate(-180)
            painter.translate(-pointie.boundingRect().center())

        # set text color used to draw message body
        ctx = QtGui.QAbstractTextDocumentLayout.PaintContext()
        if incoming:
            ctx.palette.setColor(QtGui.QPalette.Text, QtGui.QColor("black"))
        else:
            ctx.palette.setColor(QtGui.QPalette.Text, QtGui.QColor("white"))

        # draw body text
        painter.translate((0 if incoming else self.d_pointerwidth) + self.d_leftpadding, 0)
        bodydoc.documentLayout().draw(painter, ctx)

        painter.restore()

    def sizeHint(self, option, index):
        bodydoc = QtGui.QTextDocument()
        textOption = QtGui.QTextOption(bodydoc.defaultTextOption())
        textOption.setWrapMode(QtGui.QTextOption.WrapAtWordBoundaryOrAnywhere)
        bodydoc.setDefaultTextOption(textOption)
        bodydoc.setDefaultFont(QtGui.QFont("Roboto", 12))
        bodytext = index.data(QtCore.Qt.DisplayRole)
        bodydoc.setHtml(bodytext)

        # the width of the contents are the (a fraction of the window width) minus (margins + padding + width of the bubble's tail)
        contentswidth = option.rect.width() * self.d_widthfraction - self.d_horizontalmargin - self.d_pointerwidth - self.d_leftpadding - self.d_rightpadding

        # set this available width on the text document
        bodydoc.setTextWidth(contentswidth)

        size = QtCore.QSize(
            bodydoc.idealWidth() + self.d_horizontalmargin + self.d_pointerwidth + self.d_leftpadding + self.d_rightpadding,
            bodydoc.size().height() + self.d_bottompadding + self.d_toppadding + self.d_verticalmargin + 1)  # I dont remember why +1, haha, might not be necessary

        if index.row() == 0:  # have extra margin at top of first item
            size += QtCore.QSize(0, self.d_verticalmargin)

        return size
