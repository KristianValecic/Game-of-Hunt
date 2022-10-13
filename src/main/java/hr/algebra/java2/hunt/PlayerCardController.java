//package hr.algebra.java2.hunt;
//
//import javafx.fxml.FXML;
//import javafx.scene.Parent;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.FlowPane;
//
//public class PlayerCardController {
//
//    private StartMenuController parent;
//
//    private static Label lblPlayerCounterFormParent;
//
//    private static FlowPane flpnParentToPlayerCard;
//
//    private static Button btnAddPlayerFromParent;
//
//    public void setParentController(StartMenuController startMenuController) {
//        this.parent = startMenuController;
//    }
//
//    @FXML
//    protected void onClickRemovePlayer() {
//
//        flpnParentToPlayerCard.getChildren().remove(playerCounter == MAX_PLAYERS ? playerCounter-1 : playerCounter);
//
//        if(playerCounter == MAX_PLAYERS){
//            flpnParentToPlayerCard.getChildren().add(0, btnAddPlayerFromParent);
//        }
//        playerCounter--;
//        lblPlayerCounterFormParent.setText(playerCounter + DELIMTER + MAX_PLAYERS);
//    }
//}
