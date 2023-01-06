package hr.algebra.java2.message;

import hr.algebra.java2.model.Player;

import java.io.Serializable;

public class Message implements Serializable {
    private static final String DELIM = ":";
    private Player player;
    private String content;

    public Message() {
    }

    public Message (Player player, String messageContent){
        this.player = player;
        this.content = messageContent;
    }

    public Player getPlayer() {
        return player;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return player + DELIM + " " + content;
    }

    public static Message createMessageFromPlayer(Player player, String messageContent) {
        return new Message(player, messageContent);
    }
}
