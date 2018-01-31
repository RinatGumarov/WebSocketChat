package ru.innopolis.rinatgumarov.chat.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ChatClient {
    private static final int PORT = 8080;
    private DatagramSocket socket;
    private InetAddress address;

    public ChatClient() throws UnknownHostException, SocketException {
        this.address = InetAddress.getLocalHost();
        this.socket = new DatagramSocket();
    }

    public void startClient() {
        new Thread(new MessageSender(socket, address, PORT)).start();
        new Thread(new Receiver(socket)).start();
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        new ChatClient().startClient();
    }
}
