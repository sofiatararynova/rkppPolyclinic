module ru.kafpin.rkpppolyclinic {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.kafpin.rkpppolyclinic to javafx.fxml;
    exports ru.kafpin.rkpppolyclinic;
}