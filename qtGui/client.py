import socket
import json
from typing import Tuple, List


class Client:
    info_splitter = '_'

    def __init__(self):
        self.client = socket.socket()
        # connect to the server
        self.client.connect(('127.0.0.1', 1978))
        self.todo = []
        self.received = []
        self.not_end = True

    def connect(self) -> Tuple[List[str], List[dict]]:
        """connect the client to the server, get back important initial information"""
        first_info = self.btos(self.client.recv(8192)).split(self.info_splitter)
        # get the important information into a dictionary from JSON
        start_info = json.loads(first_info[1])
        # give back the important information
        return start_info['board'], start_info['pieces']

    def format_command(self, command):
        return self.info_splitter.join(command) + '\n'

    def mainloop(self):
        """main communication with the server"""
        while True:
            # check if there is any messages for the server
            if self.todo:
                # send the first message to the server
                command = self.format_command(self.todo.pop(0))
                if command != '\n':
                    self.client.send(bytes(command))
                info = self.btos(self.client.recv(8192)).split(self.info_splitter)
                # TODO is there more code to add? any reason not to make these two together?
                # parse message based on type
                if info[0] == '5':
                    piece_info = json.loads(info[1])
                    self.received.append((info[0], piece_info))
                if info[0] == '4' or info[0] == '6':
                    piece_info = json.loads(info[1])
                    self.received.append((info[0], piece_info))

    def end(self):
        """close everything that's still running"""
        self.client.close()

    def set(self, bool1):
        self.not_end = bool1

    @staticmethod
    def btos(byte: bytes) -> str:
        return byte.decode('utf-8')
