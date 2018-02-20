package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Patient {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty surname;
    private final SimpleStringProperty addr1;
    private final SimpleStringProperty addr2;
    private final SimpleStringProperty county;
    private final SimpleStringProperty pCode;
    private final SimpleStringProperty dob;
    private final SimpleIntegerProperty number;
    private final SimpleStringProperty kin;
    private final SimpleIntegerProperty kinNum;
    private final SimpleStringProperty risk;

    public Patient(int id, String fName, String sName, String addr1, String addr2, String county, String postcode, String dob, int number, String kin, int kinNum, String risk) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(fName);
        this.surname = new SimpleStringProperty(sName);
        this.addr1 = new SimpleStringProperty(addr1);
        this.addr2 = new SimpleStringProperty(addr2);
        this.county = new SimpleStringProperty(county);
        this.pCode = new SimpleStringProperty(postcode);
        this.dob = new SimpleStringProperty(dob);
        this.number = new SimpleIntegerProperty(number);
        this.kin = new SimpleStringProperty(kin);
        this.kinNum = new SimpleIntegerProperty(kinNum);
        this.risk = new SimpleStringProperty(risk);
    }

}
