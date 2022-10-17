package hr.algebra.java2.model;

public class Move {
    private String playerName;
    private String playerRole;
    private String move;

    public Move(Player player, String move){
        setPlayerName(player);
        setPlayerRole(player);
        this.move = move;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(Player player) {
        this.playerName = player.getPlayerName();
    }
    public String getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(Player player) {
        this.playerRole= player.getPlayerRole().toString();
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }
}
