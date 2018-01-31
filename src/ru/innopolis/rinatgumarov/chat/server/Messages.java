package ru.innopolis.rinatgumarov.chat.server;

import java.util.List;

public class Messages {
    public String newResponse(List<Room> rooms) {
        StringBuilder sb = new StringBuilder("List of available rooms:\n");
        for (Room room : rooms) {
            sb.append(room.getName()).append("\n");
        }
        return sb.toString();
    }
}
