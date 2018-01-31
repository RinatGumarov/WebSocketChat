package ru.innopolis.rinatgumarov.chat.server;

import java.util.HashSet;
import java.util.Set;

public class Room {
    private Set<Client> clients;
    private Set<String> clientNames;
    private final String name;
    private int counter;

    public Room(String name) {
        this.name = name;
        this.clients = new HashSet<>();
        this.clientNames = new HashSet<>();
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void addClient(Client client) {
        this.clients.add(client);
        setNameForNewy(client);
    }

    private void setNameForNewy(Client client){
        String name;
        do {
            name = "Client" + String.valueOf(++counter);
            client.setName(name);
        } while (clientNames.contains(name));
        clientNames.add(name);
    }

    public Client removeClient(Client client) {
        this.clients.remove(client);
        this.clientNames.remove(client.getName());
        return client;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public boolean contains(Client client) {
        return this.clients.contains(client);
    }

    public String getName() {
        return name;
    }

}
