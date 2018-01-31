package ru.innopolis.rinatgumarov.chat.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.Charset;

public class Receiver implements Runnable {
    private DatagramSocket socket;
    private static final int BUFFER_SIZE = 1024;
    private static final Charset utf8 = Charset.forName("UTF-8");
    private static final int MAX_FAILS = 3;

    public Receiver(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        for (int fails = 0;;) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                fails = 0;
            } catch (IOException e) {
                if (++fails > MAX_FAILS){
                    System.err.println("Could not connect to the server. Good luck next time;)");
                }
                System.out.println("Trying to reconnect to the server");
                continue;
            }

            String s = new String(packet.getData(), 0, packet.getLength(), utf8);
            System.out.println(s);
        }
    }
}
