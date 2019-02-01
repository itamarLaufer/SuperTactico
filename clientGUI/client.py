import socket
import json_parser
import pprint
import time


class Client:
    def __init__(self):
        self.client = socket.socket()
        self.client.connect(('127.0.0.1', 1978))

    def connect(self):
        first_info = self.client.recv(8192).split('_')
        print first_info[0]
        start_info = json_parser.json_loads_byteified(first_info[1])
        # start_info = json.loads(first_info[1])
        print start_info.keys()
        return start_info['board'], start_info['pieces']

    def mainloop(self):
        pass

    def end(self):
        self.client.close()
