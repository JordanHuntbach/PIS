package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.sql.*;
import java.time.LocalDate;

public class Controller {

    @FXML
    public TableView patientsTable;

    @FXML
    public TextField patientID;

    @FXML
    public TextField patientFirstName;

    @FXML
    public TextField patientLastName;

    @FXML
    public DatePicker patientDOB;

    @FXML
    public CheckBox check_address;

    @FXML
    public CheckBox check_contact;

    @FXML
    public CheckBox check_nok;

    @FXML
    public CheckBox check_nok_contact;

    @FXML
    public CheckBox check_risk;

    @FXML
    public CheckBox check_comments;

    private Connection conn;

    public Controller() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://mysql.dur.ac.uk/Pkdkj55_Patient_Monitoring?" + "user=kdkj55&password=nor74th");
            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    private String getSQL() {
        String sql = "SELECT Patients.id, Patients.first_name, Patients.surname, Patients.dob";

        if (check_address.isSelected()) {
            sql += ", Patients.address1, Patients.address2, Patients.county, Patients.postcode";
        }
        if (check_contact.isSelected()) {
            sql += ", Patients.contact_number";
        }
        if (check_nok.isSelected()) {
            sql += ", Patients.next_of_kin";
        }
        if (check_nok_contact.isSelected()) {
            sql += ", Patients.kin_number";
        }
        if (check_risk.isSelected()) {
            sql += ", Patients.risk";
        }
        if (check_comments.isSelected()) {
            sql += ", Patients.comments";
        }

        sql += " FROM Patients ";

        String id = patientID.getText(); // Result is empty string "" if none.
        String fName = patientFirstName.getText();
        String lName = patientLastName.getText();
        LocalDate dobValue = patientDOB.getValue();
        String dob = "";
        if (dobValue != null) {
            dob = dobValue.toString();
        }

        Boolean where = false;
        if (!id.equals("")) {
            sql += "WHERE Patients.id = '" + id + "' ";
            where = true;
        }
        if (!fName.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Patients.first_name = '" + fName + "' ";
        }
        if (!lName.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Patients.surname = '" + lName + "' ";
        }
        if (!dob.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Patients.dob = '" + dob + "' ";
        }
        return sql;
    }

    public void getPatients() {
        try {
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(getSQL());

            ObservableList<ObservableList> data = FXCollections.observableArrayList();
            // TABLE COLUMN ADDED DYNAMICALLY
            patientsTable.getColumns().clear();
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
                patientsTable.getColumns().addAll(col);
                System.out.println("Created column ["+i+"] ");
            }

            // Data added to ObservableList
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row added: "+row );
                data.add(row);

            }
            patientsTable.setItems(data);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public void getConsultations() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Consultations");
            while (rs.next()) {
                String name = rs.getString("first_name");
                System.out.println(name);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
}
