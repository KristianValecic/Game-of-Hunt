package hr.algebra.java2.model;

import java.util.Objects;
import java.util.Optional;

public class Move {
    private String playerName;
    private String playerRole;
    private String event;
    private Coordinate coordinate;

    public Move(Player player, String event, Coordinate coordinate){
        setPlayerName(player);
        setPlayerRole(player);
        this.event = event;
        this.coordinate = coordinate;
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
        return event;
    }
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(playerName, move.playerName) && Objects.equals(playerRole, move.playerRole) && Objects.equals(event, move.event) && Objects.equals(coordinate, move.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, playerRole, event, coordinate);
    }
}
