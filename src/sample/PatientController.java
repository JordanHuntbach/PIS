package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PatientController {

    @FXML
    public TextField number;
    public TextField nokNumber;


    public void initialize() {
        number.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                number.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        nokNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                number.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

}
