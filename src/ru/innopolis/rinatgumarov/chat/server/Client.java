package ru.innopolis.rinatgumarov.chat.server;

import java.net.InetAddress;

public class Client {
    private final InetAddress inetAddress;
    private final int port;
    private String name;
    private Room room;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Client(InetAddress inetAddress, int port, String name) {
        this.inetAddress = inetAddress;
        this.port = port;
        this.name = name;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int hashCode() {
        return 31 * this.inetAddress.hashCode() + this.port;
    }

    public boolean equals(Object o){
        if (o == null || !(o instanceof Client))
            return false;
        Client client = (Client) o;
        return this.port == client.getPort() && this.inetAddress.equals(client.getInetAddress());
    }
}
