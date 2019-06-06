from PySide2 import QtWidgets, QtGui
import chat_message_box


class Chat(QtWidgets.QVBoxLayout):
    def __init__(self, queue, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.loc = 0
        self.messages = chat_message_box.ChatMessagesBox()
        self.message = QtWidgets.QLineEdit()

        self.addWidget(self.messages)
        self.addWidget(self.message)

        QtWidgets.QShortcut(QtGui.QKeySequence('return'), self.message, self.send)
        self.queue = queue

    def send(self):
        text = self.message.text()
        self.messages.add_good(text)
        self.queue.put_nowait(['8', text])
        self.message.setText('')

    def receive(self, text):
        self.messages.add_bad(text)
