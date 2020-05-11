import socket
import pygame
from sense_hat import SenseHat
from pygame import mixer

sense = SenseHat()
sense.clear()
mixer.init()  # Geeft error als er geen audio device is

host = ''
port = 8000

running = False

lastPlayedSong = ""

fail = "fail"
success = "success"

o = (0, 0, 0)
W = (50, 50, 50)
Y = (100, 100, 0)
y = (50, 50, 0)
lamp = [
    o, o, y, o, o, y, o, o,
    o, o, o, o, o, o, o, o,
    y, o, o, Y, Y, o, o, y,
    o, o, Y, Y, Y, Y, o, o,
    y, o, Y, Y, Y, Y, o, y,
    o, o, o, Y, Y, o, o, o,
    o, y, o, W, W, o, y, o,
    o, o, o, o, o, o, o, o
]


def setup_server():
    global running

    new_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    hostname = socket.gethostname()
    print("Socket created at " + hostname + " on port " + str(port))

    try:
        new_socket.bind((host, port))
        print("Socket bind complete.")
        running = True
    except socket.error as msg:
        print("setup_server: " + str(msg))

    return new_socket


def setup_connection():
    s.listen(1)  # staat maar 1 connectie tegelijkertijd toe
    connection, address = s.accept()
    print("Connected to: " + address[0] + ":" + str(address[1]))
    return connection


def data_transfer(conn):
    global running

    # loop die data verstuurt en ontvangt totdat verteld wordt te stoppen
    while True:
        reply = None

        # ontvang de data
        data = conn.recv(1024).decode('utf-8')
        print("> " + data)

        # split het bericht op in losse commands
        commands = data.split(' ', 2)

        if commands[0] == 'METING':
            temp = round(sense.get_temperature(), 2)
            humidity = round(sense.get_humidity())
            pressure = round(sense.get_pressure())
            reply = str(temp) + ' ' + str(humidity) + ' ' + str(pressure)

        elif commands[0] == 'MUSIC':
            reply = MUSIC(commands)

        elif commands[0] == 'LAMP':
            reply = LAMP(commands)

        elif commands[0] == 'KILL':
            print("> " + data)
            print("Server shutting down...")
            s.close()
            running = False
            break

        # stuur het antwoord terug naar de client
        if reply is None:
            reply = fail
        conn.sendall(str.encode(reply))
        print(reply)


def LAMP(commands):

    if len(commands) == 1:  # geen subcommand
        return

    if commands[1] == 'ON':
        sense.set_pixels(lamp)
        return success

    elif commands[1] == 'OFF':
        sense.clear()
        return success


def MUSIC(commands):
    global lastPlayedSong

    if len(commands) == 1:  # geen subcommand
        return

    if commands[1] == 'PAUSE':
        mixer.music.pause()
        return success

    elif commands[1] == 'UNPAUSE':
        mixer.music.unpause()
        return success

    elif commands[1] == 'SET_TIME':
        if len(commands) == 2:  # geen timestamp
            return
        if not mixer.music.get_busy():
            mixer.music.load(lastPlayedSong)
            mixer.music.play()
        mixer.music.rewind()
        mixer.music.set_pos(float(commands[2]))
        return success

    elif commands[1] == 'PLAY':
        if len(commands) == 2:  # geen bestandsnaam
            return
        try:
            lastPlayedSong = '/home/pi/Music/' + commands[2] + '.mp3'
            mixer.music.load(lastPlayedSong)
            mixer.music.play()
            return success
        except pygame.error as _:
            return 'Kon "' + commands[2] + '.mp3" niet vinden. '


s = setup_server()

while running:
    try:
        conn = setup_connection()
        data_transfer(conn)
    except ConnectionResetError:
        print("Verbinding door client verbroken.")
        mixer.music.stop()
        sense.clear()
