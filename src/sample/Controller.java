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

    // Patients Tab
    @FXML
    public TableView patientsTable;
    public TextField patientID;
    public TextField patientFirstName;
    public TextField patientLastName;
    public DatePicker patientDOB;
    public CheckBox check_address;
    public CheckBox check_contact;
    public CheckBox check_nok;
    public CheckBox check_nok_contact;
    public CheckBox check_risk;
    public CheckBox check_comments;

    // Diagnoses Tab
    @FXML
    public TableView diagnosesTable;
    public TextField diagnosisID;
    public TextField diagnosisPID;
    public TextField diagnosisFName;
    public TextField diagnosisLName;
    public TextField diagnosisConsultant;
    public TextField diagnosisCondition;
    public DatePicker diagnosisDate;


    // Prescriptions Tab
    @FXML
    public TableView prescriptionTable;
    public TextField prescriptionID;
    public TextField prescriptionPID;
    public TextField prescriptionFName;
    public TextField prescriptionLName;
    public TextField prescriptionConsultant;
    public TextField prescriptionMeds;
    public DatePicker prescriptionDate;


    // Consultations Tab
    @FXML
    public TableView consultationTable;
    public TextField consultationID;
    public TextField consultationPID;
    public TextField consultationFName;
    public TextField consultationLName;
    public DatePicker consultationDate;
    public TextField consultationConsultant;
    public TextField consultationLocation;

    // Treatments Tab
    @FXML
    public TableView treatmentTable;
    public TextField treatmentID;
    public TextField treatmentPID;
    public TextField treatmentFName;
    public TextField treatmentLName;
    public DatePicker treatmentDate;
    public TextField treatmentConsultant;
    public TextField treatmentTreatment;

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

    private void fillTable(TableView table, ResultSet results) throws SQLException {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        // TABLE COLUMN ADDED DYNAMICALLY
        table.getColumns().clear();
        for(int i=0 ; i<results.getMetaData().getColumnCount(); i++){
            //We are using non property style for making dynamic table
            final int j = i;
            TableColumn col = new TableColumn(results.getMetaData().getColumnName(i+1));
            // TODO: Map sql column name to a nicer one.
            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
            table.getColumns().addAll(col);
            System.out.println("Created column ["+i+"] ");
        }

        // Data added to ObservableList
        while(results.next()){
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=results.getMetaData().getColumnCount(); i++){
                //Iterate Column
                row.add(results.getString(i));
            }
            System.out.println("Row added: "+row );
            data.add(row);
        }
        table.setItems(data);
    }

    private String getPatientsSQL() {
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
            ResultSet rs = stmt.executeQuery(getPatientsSQL());
            fillTable(patientsTable, rs);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    private String getDiagnosesSQL() {
        String sql = "SELECT Diagnoses.id, Diagnoses.patient_id, Patients.first_name, Patients.surname, Diagnoses.date, Conditions.condition, Consultants.name, Diagnoses.comment " +
                "FROM Diagnoses " +
                "INNER JOIN Patients ON Diagnoses.patient_id = Patients.id " +
                "INNER JOIN Conditions ON Diagnoses.condition = Conditions.id " +
                "INNER JOIN Consultants ON Diagnoses.diagnostician = Consultants.id ";

        String id = diagnosisID.getText();
        String pid = diagnosisPID.getText();
        String fName = diagnosisFName.getText();
        String lName = diagnosisLName.getText();
        String consultant = diagnosisConsultant.getText();
        String condition = diagnosisCondition.getText();
        LocalDate dateValue = diagnosisDate.getValue();
        String date = "";
        if (dateValue != null) {
            date = dateValue.toString();
        }

        Boolean where = false;
        if (!id.equals("")) {
            sql += "WHERE Diagnoses.id = '" + id + "' ";
            where = true;
        }
        if (!pid.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Diagnoses.patient_id = '" + pid + "' ";
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
        if (!consultant.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Consultants.name = '" + consultant + "' ";
        }
        if (!condition.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Conditions.condition = '" + condition + "' ";
        }
        if (!date.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Diagnoses.date = '" + date + "' ";
        }
        return sql;
    }

    public void getDiagnoses() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getDiagnosesSQL());
            fillTable(diagnosesTable, rs);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public String getPrescriptionsSQL() {
        String sql = "SELECT Prescriptions.id, Prescriptions.patient_id, Patients.first_name, Patients.surname, Prescriptions.date, Medications.medication, Consultants.name, Prescriptions.comment " +
                "FROM Prescriptions " +
                "INNER JOIN Patients ON Prescriptions.patient_id = Patients.id " +
                "INNER JOIN Medications ON Prescriptions.medication = Medications.id " +
                "INNER JOIN Consultants ON Prescriptions.prescriber = Consultants.id ";

        String id = prescriptionID.getText();
        String pid = prescriptionPID.getText();
        String fName = prescriptionFName.getText();
        String lName = prescriptionLName.getText();
        String consultant = prescriptionConsultant.getText();
        String medication = prescriptionMeds.getText();
        LocalDate dateValue = prescriptionDate.getValue();
        String date = "";
        if (dateValue != null) {
            date = dateValue.toString();
        }

        Boolean where = false;
        if (!id.equals("")) {
            sql += "WHERE Prescriptions.id = '" + id + "' ";
            where = true;
        }
        if (!pid.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Prescriptions.patient_id = '" + pid + "' ";
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
        if (!consultant.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Consultants.name = '" + consultant + "' ";
        }
        if (!medication.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Medications.medication = '" + medication + "' ";
        }
        if (!date.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Prescriptions.date = '" + date + "' ";
        }
        return sql;
    }

    public void getPrescriptions() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getPrescriptionsSQL());
            fillTable(prescriptionTable, rs);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public String getConsultationsSQL() {
        String sql = "SELECT Consultations.id, Consultations.patient_id, Patients.first_name, Patients.surname, Consultations.date, Consultations.time,  Consultants.name, `GP Practices`.name, Consultations.comment " +
                "FROM  Consultations " +
                "INNER JOIN Patients ON Consultations.patient_id = Patients.id " +
                "INNER JOIN `GP Practices` ON Consultations.location = `GP Practices`.id " +
                "INNER JOIN Consultants ON Consultations.consultant = Consultants.id ";

        String id = consultationID.getText();
        String pid = consultationPID.getText();
        String fName = consultationFName.getText();
        String lName = consultationLName.getText();
        String consultant = consultationConsultant.getText();
        String location = consultationLocation.getText();
        LocalDate dateValue = consultationDate.getValue();
        String date = "";
        if (dateValue != null) {
            date = dateValue.toString();
        }

        Boolean where = false;
        if (!id.equals("")) {
            sql += "WHERE Consultations.id = '" + id + "' ";
            where = true;
        }
        if (!pid.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Consultations.patient_id = '" + pid + "' ";
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
        if (!consultant.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Consultants.name = '" + consultant + "' ";
        }
        if (!location.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "`GP Practices`.name = '" + location + "' ";
        }
        if (!date.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Consultations.date = '" + date + "' ";
        }

        return sql;
    }

    public void getConsultations() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getConsultationsSQL());
            fillTable(consultationTable, rs);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    private String getTreatmentsSQL() {
        String sql = "SELECT Treatments.id, Treatments.patient_id, Patients.first_name, Patients.surname, Treatment.treatment, Treatments.date_started, Consultants.name, Treatments.comment " +
                "FROM  Treatments " +
                "INNER JOIN Patients ON Treatments.patient_id = Patients.id " +
                "INNER JOIN Treatment ON Treatments.treatment = Treatment.id " +
                "INNER JOIN Consultants ON Treatments.consultant = Consultants.id ";

        String id = treatmentID.getText();
        String pid = treatmentPID.getText();
        String fName = treatmentFName.getText();
        String lName = treatmentLName.getText();
        String consultant = treatmentConsultant.getText();
        String treatment = treatmentTreatment.getText();
        LocalDate dateValue = treatmentDate.getValue();
        String date = "";
        if (dateValue != null) {
            date = dateValue.toString();
        }

        Boolean where = false;
        if (!id.equals("")) {
            sql += "WHERE Treatments.id = '" + id + "' ";
            where = true;
        }
        if (!pid.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Treatments.patient_id = '" + pid + "' ";
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
        if (!consultant.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Consultants.name = '" + consultant + "' ";
        }
        if (!treatment.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Treatment.treatment = '" + treatment + "' ";
        }
        if (!date.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Treatments.date_started = '" + date + "' ";
        }

        return sql;
    }

    public void getTreatments() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getTreatmentsSQL());
            fillTable(treatmentTable, rs);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
}