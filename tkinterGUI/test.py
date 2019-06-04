import subprocess
import threading
from termcolor import colored
from time import sleep


def do_stdout(proc, color):
    while True:
        p = proc.readline().strip()
        if p and 'com.laufer' not in p:
            print(colored(p, color))


server = subprocess.Popen(['java', '-jar', r'..\stEngine\stEngine.jar'], stdout=subprocess.PIPE)
sleep(1)
client1 = subprocess.Popen(['python', 'gui_main.py'], stdout=subprocess.PIPE)
client2 = subprocess.Popen(['python', 'gui_main.py'], stdout=subprocess.PIPE)

run_list = (server.stdout, client1.stdout, client2.stdout)
colors = ('blue', 'magenta', 'yellow')
threads = []
try:
    for i, color in zip(run_list, colors):
        t = threading.Thread(target=do_stdout, args=(i, color))
        t.start()
        threads.append(t)
    for i in threads:
        i.join()
finally:
    server.kill()
    client1.kill()
    client2.kill()