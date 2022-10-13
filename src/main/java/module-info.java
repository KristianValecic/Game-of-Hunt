module hr.algebra.java2.hunt {
    requires javafx.controls;
    requires javafx.fxml;


    opens hr.algebra.java2.hunt to javafx.fxml;
    exports hr.algebra.java2.hunt;
}