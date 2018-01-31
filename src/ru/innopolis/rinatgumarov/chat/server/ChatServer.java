package ru.innopolis.rinatgumarov.chat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private DatagramSocket datagramSocket;
    private Map<String, Room> rooms;
    private Map<Client, Room> clientRoomMap;
    private Map<Client, Client> clients;
    private static Charset utf8 = Charset.forName("UTF-8");
    private static final int BUFFER_LENGTH = 1024;
    private static final int MAX_TRIES = 5;

    public ChatServer(int port) {
        this.rooms = new HashMap<>();
        this.clients = new HashMap<>();
        this.clientRoomMap = new HashMap<>();
        this.rooms.put("Root_room", new Room("Root_room"));
        try {
            this.datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.err.println("Could not start server on port " + port);
        }
    }

    public void startServer() {
        byte[] buffer = new byte[BUFFER_LENGTH];
        while (true) {
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            try {
                datagramSocket.receive(datagramPacket);
            } catch (IOException e) {
                System.err.println("Could not receive packet");
                continue;
            }
            Client from = new Client(datagramPacket.getAddress(), datagramPacket.getPort(), "");
            if (clients.containsKey(from))
                from = clients.get(from);
            else clients.put(from, from);
            String message = new String(buffer, 0, datagramPacket.getLength(), utf8);
            switch (message.trim().split("\\s")[0]) {
                case "/help":
                case "/h":
                    sendMessage(from, Responses.helpResponse());
                    break;
                case "/rooms":
                    sendMessage(from, Responses.roomsResponse(this.rooms.keySet()));
                    break;
                case "/connect":
                    if (message.split("\\s").length < 2) {
                        sendMessage(from, Responses.incorrectUseOfConnect());
                        break;
                    }
                    connectToRoom(from, message.substring(message.indexOf(" ")).trim());
                    break;
                case "/rename":
                    if (!(from.getRoom() == null)) {
                        if (message.split("\\s").length < 2) {
                            sendMessage(from, Responses.incorrectUseOfRename());
                            break;
                        }
                        rename(from, message.substring(message.indexOf(" ")).trim());
                    }
                case "/new":
                    if (message.split("\\s").length < 2) {
                        sendMessage(from, Responses.incorrectUseOfNew());
                        break;
                    }
                    createNewRoom(from, message.substring(message.indexOf(" ")).trim());
                case "/myname":
                    sendMessage(from, Responses.myName(from.getName()));
                case "/room":
                    sendMessage(from, Responses.roomName(from.getRoom().getName()));
                    break;
                case "::exit":
                    leaveChat(from);
                default:
                    if (!this.clientRoomMap.keySet().contains(from)) {
                        sendMessage(from, Responses.roomsResponse(this.rooms.keySet()));
                        break;
                    }
                    sendMessageToRoom(from.getRoom(), from, Responses.customResponse(from.getName(), message));
                    break;

            }
        }
    }

    private void leaveChat(Client client) {
        sendMessageToRoom(client.getRoom(), client, Responses.successDisconnectRoom(client.getName()));
        sendMessage(client, Responses.successDisconnectClient(client.getRoom().getName()));
    }

    private void createNewRoom(Client from, String roomName) {
        this.rooms.put(roomName, new Room(roomName));
        connectToRoom(from, roomName);
    }

    private void rename(Client from, String newName) {
        sendMessage(from, Responses.renameResponseClient(newName));
        sendMessageToRoom(from.getRoom(), from, Responses.renameResponseRoom(from, newName));
        from.setName(newName);
    }

    private void connectToRoom(Client client, String roomName) {
        if (this.clientRoomMap.remove(client) != null) {
            client.getRoom().removeClient(client);
            sendMessageToRoom(client.getRoom(), client, Responses.successDisconnectRoom(client.getName()));
            sendMessage(client, Responses.successDisconnectClient(client.getRoom().getName()));
        }
        Room r = this.rooms.get(roomName);
        if (r != null) {
            this.clientRoomMap.put(client, this.rooms.get(roomName));
            client.setRoom(r);
            r.addClient(client);
            sendMessage(client, Responses.successConnectClient(client.getName(), r.getName()));
            sendMessageToRoom(client.getRoom(), client, Responses.successConnectRoom(client.getName()));
            return;
        }
        sendMessage(client, Responses.roomNotExist(roomName));
    }

    private void sendMessageToRoom(Room room, Client from, String message) {
        for (Client client : room.getClients()) {
            if (client == from)
                continue;
            if (!sendMessage(client, message)) {
                room.removeClient(client);
            }
        }
    }

    private boolean sendMessage(Client client, String message) {
        byte[] bytes = message.getBytes(utf8);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, client.getInetAddress(), client.getPort());
        for (int tries = 0; ; ) {
            try {
                datagramSocket.send(packet);
                return true;
            } catch (IOException e) {
                if (++tries < MAX_TRIES) {
                    continue;
                }
                return false;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Usage: javac ChatServer <port>");
            return;
        }
        new ChatServer(Integer.parseInt(args[0])).startServer();
    }
}
