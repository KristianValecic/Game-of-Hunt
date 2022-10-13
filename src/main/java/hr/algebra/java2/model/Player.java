package hr.algebra.java2.model;

public class Player {
    private String playerName;
    private PlayerRole playerRole;

    public Player(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }

    public void setName(String name) {
        this.playerName = name;
    }
}
