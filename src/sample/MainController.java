package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class MainController {

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
    public TitledPane patientSPane;
    public GridPane patientAddPane;

    // Diagnoses Tab
    @FXML
    public TableView diagnosesTable;
    public TextField diagnosisID;
    public TextField diagnosisPID;
    public TextField diagnosisFName;
    public TextField diagnosisLName;
    public ChoiceBox<String> diagnosisConsultant;
    public ChoiceBox<String> diagnosisCondition;
    public DatePicker diagnosisDate;


    // Prescriptions Tab
    @FXML
    public TableView prescriptionTable;
    public TextField prescriptionID;
    public TextField prescriptionPID;
    public TextField prescriptionFName;
    public TextField prescriptionLName;
    public ChoiceBox<String> prescriptionConsultant;
    public ChoiceBox<String> prescriptionMeds;
    public DatePicker prescriptionDate;


    // Consultations Tab
    @FXML
    public TableView consultationTable;
    public TextField consultationID;
    public TextField consultationPID;
    public TextField consultationFName;
    public TextField consultationLName;
    public DatePicker consultationDate;
    public ChoiceBox<String> consultationConsultant;
    public ChoiceBox<String> consultationLocation;

    // Treatments Tab
    @FXML
    public TableView treatmentTable;
    public TextField treatmentID;
    public TextField treatmentPID;
    public TextField treatmentFName;
    public TextField treatmentLName;
    public DatePicker treatmentDate;
    public ChoiceBox<String> treatmentConsultant;
    public ChoiceBox<String> treatmentTreatment;

    private Connection conn;

    public MainController() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://mysql.dur.ac.uk/Pkdkj55_Patient_Monitoring?" + "user=kdkj55&password=nor74th");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public void initialize() {
        updateTreatmentSelectors();
        updateDiagnosisSelectors();
        updateConsultationSelectors();
        updatePrescriptionSelectors();
        getPatients();
        getTreatments();
        getDiagnoses();
        getConsultations();
        getPrescriptions();
        patientAddPane.managedProperty().bind(patientAddPane.visibleProperty());
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
            sql += "Patients.first_name LIKE '" + fName + "%' ";
        }
        if (!lName.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Patients.surname LIKE '" + lName + "%' ";
        }
        if (!dob.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
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
        String sql = "SELECT Diagnoses.id, Diagnoses.patient_id, Patients.first_name, Patients.surname, Diagnoses.date, Conditions.condition, Consultants.consultant, Diagnoses.comment " +
                "FROM Diagnoses " +
                "INNER JOIN Patients ON Diagnoses.patient_id = Patients.id " +
                "INNER JOIN Conditions ON Diagnoses.condition = Conditions.id " +
                "INNER JOIN Consultants ON Diagnoses.diagnostician = Consultants.id ";

        String id = diagnosisID.getText();
        String pid = diagnosisPID.getText();
        String fName = diagnosisFName.getText();
        String lName = diagnosisLName.getText();
        String consultant = diagnosisConsultant.getValue();
        String condition = diagnosisCondition.getValue();
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
            sql += "Patients.first_name LIKE '" + fName + "%' ";
        }
        if (!lName.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Patients.surname LIKE '" + lName + "%' ";
        }
        if (!consultant.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Consultants.consultant = '" + consultant + "' ";
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

    private String getPrescriptionsSQL() {
        String sql = "SELECT Prescriptions.id, Prescriptions.patient_id, Patients.first_name, Patients.surname, Prescriptions.date, Medications.medication, Consultants.consultant, Prescriptions.comment " +
                "FROM Prescriptions " +
                "INNER JOIN Patients ON Prescriptions.patient_id = Patients.id " +
                "INNER JOIN Medications ON Prescriptions.medication = Medications.id " +
                "INNER JOIN Consultants ON Prescriptions.prescriber = Consultants.id ";

        String id = prescriptionID.getText();
        String pid = prescriptionPID.getText();
        String fName = prescriptionFName.getText();
        String lName = prescriptionLName.getText();
        String consultant = prescriptionConsultant.getValue();
        String medication = prescriptionMeds.getValue();
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
            sql += "Patients.first_name LIKE '" + fName + "%' ";
        }
        if (!lName.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Patients.surname LIKE '" + lName + "%' ";
        }
        if (!consultant.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Consultants.consultant = '" + consultant + "' ";
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

    private String getConsultationsSQL() {
        String sql = "SELECT Consultations.id, Consultations.patient_id, Patients.first_name, Patients.surname, Consultations.date, Consultations.time,  Consultants.consultant, `GP Practices`.location, Consultations.comment " +
                "FROM  Consultations " +
                "INNER JOIN Patients ON Consultations.patient_id = Patients.id " +
                "INNER JOIN `GP Practices` ON Consultations.location = `GP Practices`.id " +
                "INNER JOIN Consultants ON Consultations.consultant = Consultants.id ";

        String id = consultationID.getText();
        String pid = consultationPID.getText();
        String fName = consultationFName.getText();
        String lName = consultationLName.getText();
        String consultant = consultationConsultant.getValue();
        String location = consultationLocation.getValue();
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
            sql += "Patients.first_name LIKE '" + fName + "%' ";
        }
        if (!lName.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Patients.surname LIKE '" + lName + "%' ";
        }
        if (!consultant.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Consultants.consultant = '" + consultant + "' ";
        }
        if (!location.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "`GP Practices`.location = '" + location + "' ";
        }
        if (!date.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
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
        String sql = "SELECT Treatments.id, Treatments.patient_id, Patients.first_name, Patients.surname, Treatment.treatment, Treatments.date_started, Consultants.consultant, Treatments.comment " +
                "FROM  Treatments " +
                "INNER JOIN Patients ON Treatments.patient_id = Patients.id " +
                "INNER JOIN Treatment ON Treatments.treatment = Treatment.id " +
                "INNER JOIN Consultants ON Treatments.consultant = Consultants.id ";

        String id = treatmentID.getText();
        String pid = treatmentPID.getText();
        String fName = treatmentFName.getText();
        String lName = treatmentLName.getText();
        String consultant = treatmentConsultant.getValue();
        String treatment = treatmentTreatment.getValue();
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
            sql += "Patients.first_name LIKE '" + fName + "%' ";
        }
        if (!lName.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Patients.surname LIKE '" + lName + "%' ";
        }
        if (!consultant.equals("")) {
            if (where) {
                sql += "AND ";
            } else {
                where = true;
                sql += "WHERE ";
            }
            sql += "Consultants.consultant = '" + consultant + "' ";
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

    private void updateTreatmentSelectors() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT treatment FROM Treatment");
            ObservableList<String> treatments = FXCollections.observableArrayList();
            treatments.add("");
            while (rs.next()) {
                treatments.add(rs.getString("treatment"));
            }
            treatmentTreatment.setItems(treatments);
            treatmentTreatment.setValue("");
            treatmentTreatment.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getTreatments());

            rs = stmt.executeQuery("SELECT consultant FROM Consultants");
            ObservableList<String> consultants = FXCollections.observableArrayList();
            consultants.add("");
            while (rs.next()) {
                consultants.add(rs.getString("consultant"));
            }
            treatmentConsultant.setItems(consultants);
            treatmentConsultant.setValue("");
            treatmentConsultant.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getTreatments());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    private void updateConsultationSelectors() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT location FROM `GP Practices`");
            ObservableList<String> locations = FXCollections.observableArrayList();
            locations.add("");
            while (rs.next()) {
                locations.add(rs.getString("location"));
            }
            consultationLocation.setItems(locations);
            consultationLocation.setValue("");
            consultationLocation.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getConsultations());

            rs = stmt.executeQuery("SELECT consultant FROM Consultants");
            ObservableList<String> consultants = FXCollections.observableArrayList();
            consultants.add("");
            while (rs.next()) {
                consultants.add(rs.getString("consultant"));
            }
            consultationConsultant.setItems(consultants);
            consultationConsultant.setValue("");
            consultationConsultant.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getConsultations());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    private void updatePrescriptionSelectors() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT medication FROM Medications");
            ObservableList<String> medicines = FXCollections.observableArrayList();
            medicines.add("");
            while (rs.next()) {
                medicines.add(rs.getString("medication"));
            }
            prescriptionMeds.setItems(medicines);
            prescriptionMeds.setValue("");
            prescriptionMeds.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getPrescriptions());

            rs = stmt.executeQuery("SELECT consultant FROM Consultants");
            ObservableList<String> consultants = FXCollections.observableArrayList();
            consultants.add("");
            while (rs.next()) {
                consultants.add(rs.getString("consultant"));
            }
            prescriptionConsultant.setItems(consultants);
            prescriptionConsultant.setValue("");
            prescriptionConsultant.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getPrescriptions());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    private void updateDiagnosisSelectors() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT `condition` FROM Conditions");
            ObservableList<String> condition = FXCollections.observableArrayList();
            condition.add("");
            while (rs.next()) {
                condition.add(rs.getString("condition"));
            }
            diagnosisCondition.setItems(condition);
            diagnosisCondition.setValue("");
            diagnosisCondition.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getDiagnoses());

            rs = stmt.executeQuery("SELECT consultant FROM Consultants");
            ObservableList<String> consultants = FXCollections.observableArrayList();
            consultants.add("");
            while (rs.next()) {
                consultants.add(rs.getString("consultant"));
            }
            diagnosisConsultant.setItems(consultants);
            diagnosisConsultant.setValue("");
            diagnosisConsultant.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getDiagnoses());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public void clearPatients() {
        check_address.setSelected(false);
        check_contact.setSelected(false);
        check_nok.setSelected(false);
        check_nok_contact.setSelected(false);
        check_risk.setSelected(false);
        check_comments.setSelected(false);
        patientID.clear();
        patientFirstName.clear();
        patientLastName.clear();
        patientDOB.setValue(null);
        getPatients();
    }

    public void clearDiagnoses() {
        diagnosisID.clear();
        diagnosisPID.clear();
        diagnosisFName.clear();
        diagnosisLName.clear();
        diagnosisDate.setValue(null);
        diagnosisCondition.setValue("");
        diagnosisConsultant.setValue("");
        getDiagnoses();
    }

    public void clearPrescriptions() {
        prescriptionID.clear();
        prescriptionPID.clear();
        prescriptionFName.clear();
        prescriptionLName.clear();
        prescriptionDate.setValue(null);
        prescriptionConsultant.setValue("");
        prescriptionMeds.setValue("");
        getPrescriptions();
    }

    public void clearConsultations() {
        consultationID.clear();
        consultationPID.clear();
        consultationFName.clear();
        consultationLName.clear();
        consultationDate.setValue(null);
        consultationConsultant.setValue("");
        consultationLocation.setValue("");
        getConsultations();
    }

    public void clearTreatments() {
        treatmentID.clear();
        treatmentPID.clear();
        treatmentFName.clear();
        treatmentLName.clear();
        treatmentDate.setValue(null);
        treatmentConsultant.setValue("");
        treatmentTreatment.setValue("");
        getTreatments();
    }

    public void addPatient() throws IOException {
//        Stage stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("addPatient.fxml"));
//        stage.setTitle("Add Patient");
//        Scene scene = new Scene(root, 700, 700);
//        scene.getStylesheets().add("sample/main.css");
//        stage.setScene(scene);
//        stage.show();
    patientSPane.setExpanded(false);
    patientAddPane.setVisible(true);
    }
}