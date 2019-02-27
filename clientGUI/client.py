import socket
import json_parser
import pprint
import time


class Client:
    info_splitter = '_'

    def __init__(self):
        self.client = socket.socket()
        # connect to the server
        self.client.connect(('127.0.0.1', 1978))

    def connect(self):
        """connect the client to the server, get back important initial information"""
        first_info = self.client.recv(8192).split(self.info_splitter)
        print first_info[0]
        # get the important information into a dictionary from JSON
        start_info = json_parser.json_loads_byteified(first_info[1])
        print start_info.keys()
        # give back the important information
        return start_info['board'], start_info['pieces']

    def mainloop(self):
        """start the communication with the server"""
        pass

    def end(self):
        """close everything thats still running"""
        self.client.close()
