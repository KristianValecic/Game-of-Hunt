package hr.algebra.java2.dal;

import hr.algebra.java2.model.Player;
import hr.algebra.java2.model.PlayerRole;
import javafx.geometry.Bounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    public GameState(List<PlayerState> playerStateList) {
        this.playerStateList = playerStateList;
    }

    //private static List<Player> playerList = new ArrayList<>();
    private List<PlayerState> playerStateList = new ArrayList<>();

//    public static void setPlayerList(List<Player> playersList) {
//        playerList.addAll(playersList);
//    }

//public static List<Player> getPlayerList() {
//    return playerList;
////}
//    public void setPlayerStateList(List<Player> playersList) {
//        playersList.forEach(p -> playerStateList.add(new PlayerState(p.getPlayerRole(), p.getPlayerName())));
//    }

    public  List<PlayerState> getPlayerStateList() {
        //alivePlayerList.addAll(alivePlayersList);
        return playerStateList;
    }
}
