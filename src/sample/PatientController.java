package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class PatientController {

    @FXML
    public TextField number;
    public TextField nokNumber;
    public TextField fName;
    public TextField lName;
    public DatePicker dob;
    public TextField addr1;
    public TextField addr2;
    public TextField county;
    public TextField pCode;
    public TextField nok;
    public ChoiceBox risk;

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

    public void submit() {



        //INSERT INTO `Pkdkj55_Patient_Monitoring`.`Patients` (`id`, `first_name`, `surname`, `dob`, `address1`, `address2`, `county`, `postcode`, `contact_number`, `next_of_kin`, `kin_number`, `risk`, `comments`) VALUES (NULL, 'Jordan', 'Huntbach', '1998-02-02', 'Rockhill Farm', 'Greete', 'Shropshire', 'SY8 3BT', '07538299653', 'Gaynor Huntbach', '07805854204', 'Low', 'He seems fine.');

    }
}
