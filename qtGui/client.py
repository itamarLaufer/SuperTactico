import socket
import json
from typing import Tuple, List
import asyncio


class Client:
    info_splitter = '_'

    def __init__(self):
        self.client = asyncio.open_connection('127.0.0.1', 1978)
        self.client_reader = None
        self.client_writer = None
        self.todo = asyncio.Queue()
        self.received = asyncio.Queue()

    async def connect(self) -> Tuple[List[str], List[dict]]:
        """connect the client to the server, get back important initial information"""
        self.client_reader, self.client_writer = await self.client
        first_info = self.btos(await self.client_reader.readline()).split(self.info_splitter)
        # get the important information into a dictionary from JSON
        start_info = json.loads(first_info[1])
        # give back the important information
        return start_info['board'], start_info['pieces']

    def format_command(self, command):
        return self.info_splitter.join(command) + '\n'

    async def send(self):
        while True:
            command = self.format_command(await self.todo.get())
            self.client_writer.write(command.encode('utf-8'))
            await self.client_writer.drain()

    async def receive(self):
        while True:
            info = self.btos(await self.client_reader.readline()).split(self.info_splitter)
            if info[0] == '5':
                piece_info = json.loads(info[1])
                await self.received.put([info[0], piece_info])
            if info[0] == '4' or info[0] == '6':
                piece_info = json.loads(info[1])
                await self.received.put([info[0], piece_info])

    def end(self):
        """close everything that's still running"""
        self.client.close()

    @staticmethod
    def btos(byte: bytes) -> str:
        return byte.decode('utf-8')
