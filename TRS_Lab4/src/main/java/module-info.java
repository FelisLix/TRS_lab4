module trs.trs_lab4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires static lombok;
    requires java.sql;

    opens trs.trs_lab4 to javafx.fxml;
    exports trs.trs_lab4;
}