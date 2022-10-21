package hr.algebra.java2.dal;

import hr.algebra.java2.model.Player;
import hr.algebra.java2.model.PlayerRole;
import javafx.geometry.Bounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
   private int timerState;
   private int roundState;
   private int scoreState;

   private List<PlayerState> playerStateList = new ArrayList<>();


}
