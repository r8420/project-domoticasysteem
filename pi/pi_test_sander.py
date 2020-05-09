import socket

host = ''
port = 8000

storedValue = "Dit is het opgeslagen bericht"


def setup_server():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    print("Socket created at " + socket.gethostname() + " on port " + str(port))
    try:
        s.bind((host, port))
    except socket.error as msg:
        print("setup_server() socket.error: ", end="")
        print(msg)
    print("Socket bind complete.")
    return s


def setup_connection():
    s.listen(1)  # staat maar 1 connectie tegelijkertijd toe
    conn, address = s.accept()
    print("Connected to: " + address[0] + ":" + str(address[1]))
    return conn


def GET():
    reply = storedValue
    return reply


def REPEAT(data_message):
    reply = data_message[1]
    return reply


def data_transfer(conn):
    # loop die data verstuurt en ontvangttotdat verteld wordt te stoppen
    while True:
        # ontvang de data
        data = conn.recv(1024)
        data = data.decode('utf-8')

        # split de data zodat het commando wordt gescheiden van de andere data
        data_message = data.split(' ', 1)
        command = data_message[0]

        if command == 'GET':
            reply = GET()
        elif command == 'REPEAT':
            reply = REPEAT(data_message)
        elif command == 'EXIT':
            print("our client has left :(")
            break
        elif command == 'KILL':
            print("Server shutting down...")
            s.close()
            break
        else:
            reply = 'Unknown command.'

        # stuur het antwoord terug naar de client
        conn.sendall(str.encode(reply))
        print("Data has been sent!")

    conn.close()


s = setup_server()

while True:
#    try:
        conn = setup_connection()
        data_transfer(conn)
#    except:
#        print("OEPS ER IS IETS FLINK MISGEGAAN")
#        break



