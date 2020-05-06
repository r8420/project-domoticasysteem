import socket
import time
from sense_hat import SenseHat
import threading
from queue import Queue
from pygame import mixer
NUMBER_OF_THREADS = 2
JOB_NUMBERS = [1, 2]
queue = Queue()
#socket set up
listensocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
port = 8000
maxConnections = 10
IP = socket.gethostname()

listensocket.bind(('',port))

listensocket.listen(maxConnections)
print("Server started at " + IP + " on port " + str(port))

(clientsocket, address) = listensocket.accept()
print("New conncection made!")

listensocket.setblocking(0)
sense = SenseHat()
sense.clear()
mixer.init()



temp = round(sense.get_temperature(), 2)
Humidity = round(sense.get_humidity(), 2)
Pressure = round(sense.get_pressure(), 2)
message = str(temp) +" "+ str(Humidity)+ " " + str(Pressure)
length = len(message.encode('utf-8'))
while length < 19:
    message = message + " "
    length = len(message.encode('utf-8'))

def outputstream ():
    temp = round(sense.get_temperature(), 2)
    Humidity = round(sense.get_humidity())
    Pressure = round(sense.get_pressure())
    message = str(temp) +" "+ str(Humidity)+ " " + str(Pressure)
    print(message)
    length = len(message.encode('utf-8'))
    while length < 19:
        message = message + " "
        length = len(message.encode('utf-8'))
    clientsocket.send(bytes(message, "utf-8"))

def inputstream ():
    try:
        time.sleep(0.5)
        messageJava = clientsocket.recv(1024).decode('utf-8')
        print(messageJava)
        if messageJava == "pause":
            mixer.music.pause()
        elif messageJava == "unpause":
            mixer.music.unpause()
        elif messageJava == "read sensors":
            print("doing nothing")
        else:
            mixer.music.load('/home/pi/Music/' + messageJava + '.mp3')
            mixer.music.play()
            
            
    except socket.error:
        print("error")
        
    else:
        print("mislukt")

def create_workers():
    for _ in range(NUMBER_OF_THREADS):
        t = threading.Thread(target=work)
        t.daemen = True
        t.start()

def work():
    while True:
        x = queue.get()
        if x == 1:
            outputstream()
            time.sleep(1)
        if x == 2:
            inputstream()
            time.sleep(1)

        queue.task_done()
        


def create_jobs():
    for x in JOB_NUMBERS:
        queue.put(x)

    queue.join()
while True:
    create_workers()
    create_jobs()


