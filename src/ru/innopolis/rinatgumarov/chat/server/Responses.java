package ru.innopolis.rinatgumarov.chat.server;

import java.util.Set;

public class Responses {
    public static String roomsResponse(Set<String> rooms) {
        StringBuilder sb = new StringBuilder("List of available rooms:\n");
        for (String room : rooms) {
            sb.append(room).append("\n");
        }
        return sb.toString();
    }

    public static String helpResponse(){
        return "/help - get all commands\n" +
                "/h - same as help" +
                "/connect <room name> - connect to room with name <room name>\n" +
                "/rename <new name> - change your name in this room\n" +
                "/rooms - lists all available rooms\n" +
                "/new <new room name> - create new room with name <new room name>\n" +
                "/myname - shows your name\n" +
                "/room - shows current room name\n" +
                "/::exit - leave chat";
    }

    public static String incorrectUseOfConnect(){
        return "[Error]: /connect should be with argument <room name>\n";
    }

    public static String incorrectUseOfRename(){
        return "[Error]: /rename should be with argument <new name>\n";
    }

    public static String roomNotExist(String roomName){
        return "[Error]: room with name `" + roomName + "` doesn't exist\n";
    }

    public static String successConnectClient(String client, String room){
        return "You are now connected to room `" + room + "` with name " + "`" + client + "\n`";
    }

    public static String successConnectRoom(String client){
        return "`" + client + "`" + " connected to room\n";
    }

    public static String successDisconnectClient(String room){
        return "You are disconnected from room `" + room + "`\n";
    }

    public static String successDisconnectRoom(String client){
        return "`" + client + "`" + " left room\n";
    }

    public static String customResponse(String client, String response){
        return "[" + client + "]: " + response +"\n";
    }

    public static String renameResponseClient(String newName) {
        return "You have successfully changed your name to `" + newName + "`\n";
    }

    public static String renameResponseRoom(Client client, String newName) {
        return "`" + client + "`" + " changed his name to `" + newName + "`\n";
    }

    public static String incorrectUseOfNew() {
        return "[Error]: /new should be with argument <new room name>\n";
    }

    public static String myName(String name) {
        return "Your name is `" + name + "`\n";
    }

    public static String roomName(String name) {
        return name + "\n";
    }
}
