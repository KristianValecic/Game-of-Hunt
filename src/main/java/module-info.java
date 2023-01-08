module hr.algebra.java2.hunt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.rmi;
    requires java.naming;


    opens hr.algebra.java2.hunt to javafx.fxml;
    exports hr.algebra.java2.hunt;
    exports hr.algebra.java2.model;
    opens hr.algebra.java2.model to javafx.fxml;
    exports hr.algebra.java2.networking to java.rmi;
}