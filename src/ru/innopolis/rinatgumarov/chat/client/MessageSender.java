package ru.innopolis.rinatgumarov.chat.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

public class MessageSender implements Runnable {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private static final Charset utf8 = Charset.forName("UTF-8");
    private static final int MAX_TRIES = 3;

    public MessageSender(DatagramSocket socket, InetAddress address, int port) {
        this.socket = socket;
        this.serverAddress = address;
        this.serverPort = port;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        byte[] startMessage = "/rooms".getBytes(utf8);
        DatagramPacket packet = new DatagramPacket(startMessage, startMessage.length, serverAddress, serverPort);
        ;
        for (int i = 0; ; ) {
            try {
                socket.send(packet);
                break;
            } catch (IOException e) {
                if (++i < MAX_TRIES)
                    continue;
                System.err.println("Could not connect to server. Try to restart application.");
            }
        }

        while (true) {
            String message = sc.nextLine();
            byte[] bytes = message.getBytes(utf8);
            packet = new DatagramPacket(bytes, bytes.length, serverAddress, serverPort);

            for (int i = 0; ; ) {
                try {
                    socket.send(packet);
                    break;
                } catch (IOException e) {
                    if (++i < MAX_TRIES)
                        continue;
                    System.err.println("Could not send. Will try again");
                }
            }
        }
    }
}
