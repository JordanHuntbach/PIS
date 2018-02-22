package sample;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class MainController {

    // Patients Tab
    @FXML
    public TableView<String> patientsTable;
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

    public VBox patientAddPane;
    public TextField newPatientFName;
    public TextField newPatientLName;
    public DatePicker newPatientDOB;
    public TextField newPatientAddr1;
    public TextField newPatientAddr2;
    public TextField newPatientCounty;
    public TextField newPatientPCode;
    public TextField newPatientNum;
    public TextField newPatientNok;
    public TextField newPatientNokNum;
    public ChoiceBox<String> newPatientRisk;

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
    public TitledPane diagnosisSPane;

    public VBox diagnosisAddPane;
    public ChoiceBox<String> newDiagnosisPID;
    public ChoiceBox<String> newDiagnosisCondition;
    public DatePicker newDiagnosisDate;
    public ChoiceBox<String> newDiagnosisConsultant;
    public TextArea newDiagnosisComment;


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
    public TitledPane prescriptionSPane;

    public VBox prescriptionAddPane;
    public ChoiceBox<String> newPrescriptionPID;
    public ChoiceBox<String> newPrescriptionMedication;
    public DatePicker newPrescriptionDate;
    public ChoiceBox<String> newPrescriptionConsultant;
    public TextArea newPrescriptionComment;


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
    public TitledPane consultationSPane;

    public VBox consultationAddPane;
    public ChoiceBox<String> newConsultationPID;
    public ChoiceBox<String> newConsultationLocation;
    public DatePicker newConsultationDate;
    public ChoiceBox<String> newConsultationConsultant;
    public ChoiceBox<String> newConsultationHour;
    public ChoiceBox<String> newConsultationMinute;
    public TextArea newConsultationComment;

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
    public TitledPane treatmentSPane;

    public VBox treatmentAddPane;
    public ChoiceBox<String> newTreatmentPID;
    public ChoiceBox<String> newTreatmentTreatment;
    public DatePicker newTreatmentDate;
    public ChoiceBox<String> newTreatmentConsultant;
    public TextArea newTreatmentComment;

    // Add Tab
    public TableView<String> conditionsTable;
    public TableColumn<String, String> conditionsCol;
    public TableView<String> medicationTable;
    public TableColumn<String, String> medicationCol;
    public TableView<String> consultantsTable;
    public TableColumn<String, String> consultantsCol;
    public TableView<String> treatmentsTable;
    public TableColumn<String, String> treatmentsCol;
    public TableView<String> locationsTable;
    public TableColumn<String, String> locationsCol;
    public TextField newCondition;
    public TextField newMedication;
    public TextField newConsultant;
    public TextField newLocation;
    public TextField newTreatment;

    public TabPane tabPane;

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
        conditionsCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
        conditionsCol.setCellFactory(TextFieldTableCell.forTableColumn());
        conditionsCol.setOnEditCommit(
                t -> {
                    String old = t.getOldValue();
                    String newString = t.getNewValue();
                    String sql;
                    if (newString.equals("")) {
                        sql = "DELETE FROM `Conditions` WHERE `condition` = '" + old + "'";
                    } else {
                        sql = "UPDATE `Conditions` SET `condition` = '" + newString + "' WHERE `condition` = '" + old + "'";
                    }
                    runSQL(sql);
                    updateConditionSelectors();
                }
        );
        medicationCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
        medicationCol.setCellFactory(TextFieldTableCell.forTableColumn());
        medicationCol.setOnEditCommit(
                t -> {
                    String old = t.getOldValue();
                    String newString = t.getNewValue();
                    String sql;
                    if (newString.equals("")) {
                        sql = "DELETE FROM `Medications` WHERE `medication` = '" + old + "'";
                    } else {
                        sql = "UPDATE `Medications` SET `medication` = '" + newString + "' WHERE `medication` = '" + old + "'";
                    }
                    runSQL(sql);
                    updateMedicationSelectors();
                }
        );
        consultantsCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
        consultantsCol.setCellFactory(TextFieldTableCell.forTableColumn());
        consultantsCol.setOnEditCommit(
                t -> {
                    String old = t.getOldValue();
                    String newString = t.getNewValue();
                    String sql;
                    if (newString.equals("")) {
                        sql = "DELETE FROM `Consultants` WHERE `consultant` = '" + old + "'";
                    } else {
                        sql = "UPDATE `Consultants` SET `consultant` = '" + newString + "' WHERE `consultant` = '" + old + "'";
                    }
                    runSQL(sql);
                    updateConsultantSelectors();
                }
        );
        treatmentsCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
        treatmentsCol.setCellFactory(TextFieldTableCell.forTableColumn());
        treatmentsCol.setOnEditCommit(
                t -> {
                    String old = t.getOldValue();
                    String newString = t.getNewValue();
                    String sql;
                    if (newString.equals("")) {
                        sql = "DELETE FROM `Treatment` WHERE `treatment` = '" + old + "'";
                    } else {
                        sql = "UPDATE `Treatment` SET `treatment` = '" + newString + "' WHERE `treatment` = '" + old + "'";
                    }
                    runSQL(sql);
                    updateTreatmentSelectors();
                }
        );
        locationsCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
        locationsCol.setCellFactory(TextFieldTableCell.forTableColumn());
        locationsCol.setOnEditCommit(
                t -> {
                    String old = t.getOldValue();
                    String newString = t.getNewValue();
                    String sql;
                    if (newString.equals("")) {
                        sql = "DELETE FROM `GP Practices` WHERE `location` = '" + old + "'";
                    } else {
                        sql = "UPDATE `GP Practices` SET `location` = '" + newString + "' WHERE `location` = '" + old + "'";
                    }
                    runSQL(sql);
                    updateLocationSelectors();
                }
        );

        updateAll();

        patientAddPane.managedProperty().bind(patientAddPane.visibleProperty());
        newPatientNum.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                newPatientNum.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        newPatientNokNum.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                newPatientNokNum.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        diagnosisAddPane.managedProperty().bind(diagnosisAddPane.visibleProperty());
        prescriptionAddPane.managedProperty().bind(prescriptionAddPane.visibleProperty());
        consultationAddPane.managedProperty().bind(consultationAddPane.visibleProperty());
        treatmentAddPane.managedProperty().bind(treatmentAddPane.visibleProperty());

        newConsultationHour.setValue("");
        newConsultationMinute.setValue("");
    }

    private void runSQL(String sql) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    private void updateCell(String table, String rowID, String column, String newValue) {
        String capitalised = "";
        if (newValue.length() >= 1) {
           capitalised = newValue.substring(0, 1).toUpperCase() + newValue.substring(1);
        }
        if ((column.equals("dob") || column.equals("date") || column.equals("date_started")) && !newValue.matches("\\d{4}-\\d{2}-\\d{2}")) {
            // Invalid date.
        } else if (column.equals("postcode") && newValue.length() > 8) {
            // Invalid postcode.
        } else if ((column.equals("contact_number") || column.equals("kin_number")) && !newValue.matches("^((\\(?0\\d{4}\\)?\\s?\\d{3}\\s?\\d{3})|(\\(?0\\d{3}\\)?\\s?\\d{3}\\s?\\d{4})|(\\(?0\\d{2}\\)?\\s?\\d{4}\\s?\\d{4}))(\\s?\\#(\\d{4}|\\d{3}))?$")) {
            // Invalid phone number - regex courtesy of http://regexlib.com/UserPatterns.aspx?authorid=d95177b0-6014-4e73-a959-73f1663ae814&AspxAutoDetectCookieSupport=1
        } else if (column.equals("risk") && !(capitalised.equals("Low") || capitalised.equals("Medium") || capitalised.equals("High"))) {
            // Invalid risk
        } else if (column.equals("time") && !newValue.matches("([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?")) {
            // Invalid time
        } else if (column.equals("consultant") || column.equals("treatment") || column.equals("condition") || column.equals("location") || column.equals("medication")) {
            // Need to lookup the id of the new value.
            String lookupTable;
            String id;
            try {
                switch (column) {
                    case "consultant":
                        lookupTable = "Consultants";
                        id = idFromConsultant(newValue);
                        break;
                    case "treatment":
                        lookupTable = "Treatment";
                        id = idFromTreatment(newValue);
                        break;
                    case "condition":
                        lookupTable = "Conditions";
                        id = idFromCondition(newValue);
                        break;
                    case "location":
                        lookupTable = "GP Practices";
                        id = idFromLocation(newValue);
                        break;
                    default:
                        lookupTable = "Medications";
                        id = idFromMedication(newValue);
                        break;
                }
                if(id.equals("NONE")) {
                    String sql = "INSERT INTO `" + lookupTable + "` (`" + column + "`) VALUES ('" + newValue + "')";
                    runSQL(sql);
                    switch (column) {
                        case "consultant":
                            id = idFromConsultant(newValue);
                            break;
                        case "treatment":
                            id = idFromTreatment(newValue);
                            break;
                        case "condition":
                            id = idFromCondition(newValue);
                            break;
                        case "location":
                            id = idFromLocation(newValue);
                            break;
                        default:
                            id = idFromMedication(newValue);
                            break;
                    }
                }
                if (column.equals("consultant")) {
                    switch (table) {
                        case "Prescriptions":
                            column = "prescriber";
                            break;
                        case "Diagnoses":
                            column = "diagnostician";
                            break;
                        default:
                            column = "consultant";
                            break;
                    }
                }
                newValue = id;
                String sql = "UPDATE `" + table + "` SET `" + column + "` = '" + newValue + "' WHERE `id` = '" + rowID + "'";
                runSQL(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            // Valid change.
            String sql = "UPDATE `" + table + "` SET `" + column + "` = '" + newValue + "' WHERE `id` = '" + rowID + "'";
            runSQL(sql);
        }
        updateAll();
    }

    private void updateAll() {
        updateLocationSelectors();
        updateTreatmentSelectors();
        updateConsultantSelectors();
        updateConditionSelectors();
        updateMedicationSelectors();

        getPatients();
        getConsultations();
        getTreatments();
        getPrescriptions();
        getDiagnoses();
    }

    private void fillTable(TableView table, ResultSet results) throws SQLException {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        // TABLE COLUMN ADDED DYNAMICALLY
        int exclude = 0;
        table.getColumns().clear();
        for(int i=0 ; i<results.getMetaData().getColumnCount(); i++){
            //We are using non property style for making dynamic table
            final int j = i;
            String columnName = results.getMetaData().getColumnName(i + 1);
            TableColumn col = new TableColumn(columnName);
            if (i == 1 && columnName.equals("patient_id")) {
                exclude = 3;
            }
            if (i > exclude) {
                col.setCellFactory(TextFieldTableCell.forTableColumn());
                EventHandler<TableColumn.CellEditEvent<ObservableList,String>> handler = event -> {
                    // Need the new value, the id of the row, the name of the column, and the table to update.
                    String newValue = event.getNewValue();
                    String oldValue = event.getOldValue();
                    String id = event.getRowValue().get(0).toString();
                    String column = event.getTableColumn().getText();
                    TablePosition position = event.getTablePosition();
                    String tab = tabPane.getSelectionModel().getSelectedItem().getText();
                    updateCell(tab, id, column, newValue);
                };
                col.setOnEditCommit(handler);
            }
            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
            table.getColumns().addAll(col);
            System.out.println("Created column ["+i+"] ");
        }

        // Data added to ObservableList
        while(results.next()){
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=results.getMetaData().getColumnCount(); i++){
                String result = results.getString(i);
                row.add(result);
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

    private void updatePatientSelectors() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, first_name, surname FROM Patients");
            ObservableList<String> patients = FXCollections.observableArrayList();
            patients.add("");
            while (rs.next()) {
                String name = rs.getString("id") + ": " + rs.getString("first_name") + " " + rs.getString("surname");
                patients.add(name);
            }
            newDiagnosisPID.setItems(patients);
            newDiagnosisPID.setValue("");
            newPrescriptionPID.setItems(patients);
            newPrescriptionPID.setValue("");
            newConsultationPID.setItems(patients);
            newConsultationPID.setValue("");
            newTreatmentPID.setItems(patients);
            newTreatmentPID.setValue("");
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
            while (rs.next()) {
                treatments.add(rs.getString("treatment"));
            }
            treatmentsTable.getItems().setAll(treatments);

            treatments.add(0, "");
            treatmentTreatment.setItems(treatments);
            treatmentTreatment.setValue("");
            treatmentTreatment.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getTreatments());
            newTreatmentTreatment.setItems(treatments);
            newTreatmentTreatment.setValue("");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    private void updateLocationSelectors() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT location FROM `GP Practices`");
            ObservableList<String> locations = FXCollections.observableArrayList();
            while (rs.next()) {
                locations.add(rs.getString("location"));
            }
            locationsTable.getItems().setAll(locations);

            locations.add(0, "");
            consultationLocation.setItems(locations);
            consultationLocation.setValue("");
            consultationLocation.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getConsultations());
            newConsultationLocation.setItems(locations);
            newConsultationLocation.setValue("");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    private void updateConsultantSelectors() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT consultant FROM Consultants");
            ObservableList<String> consultants = FXCollections.observableArrayList();
            while (rs.next()) {
                consultants.add(rs.getString("consultant"));
            }
            consultantsTable.getItems().setAll(consultants);

            consultants.add(0, "");
            consultationConsultant.setItems(consultants);
            consultationConsultant.setValue("");
            consultationConsultant.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getConsultations());
            prescriptionConsultant.setItems(consultants);
            prescriptionConsultant.setValue("");
            prescriptionConsultant.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getPrescriptions());
            diagnosisConsultant.setItems(consultants);
            diagnosisConsultant.setValue("");
            diagnosisConsultant.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getDiagnoses());
            treatmentConsultant.setItems(consultants);
            treatmentConsultant.setValue("");
            treatmentConsultant.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getTreatments());
            newDiagnosisConsultant.setItems(consultants);
            newDiagnosisConsultant.setValue("");
            newPrescriptionConsultant.setItems(consultants);
            newPrescriptionConsultant.setValue("");
            newConsultationConsultant.setItems(consultants);
            newConsultationConsultant.setValue("");
            newTreatmentConsultant.setItems(consultants);
            newTreatmentConsultant.setValue("");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    private void updateMedicationSelectors() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT medication FROM Medications");
            ObservableList<String> medicines = FXCollections.observableArrayList();
            while (rs.next()) {
                medicines.add(rs.getString("medication"));
            }
            medicationTable.getItems().setAll(medicines);

            medicines.add(0, "");
            prescriptionMeds.setItems(medicines);
            prescriptionMeds.setValue("");
            prescriptionMeds.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getPrescriptions());
            newPrescriptionMedication.setItems(medicines);
            newPrescriptionMedication.setValue("");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    private void updateConditionSelectors() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT `condition` FROM Conditions");
            ObservableList<String> condition = FXCollections.observableArrayList();
            while (rs.next()) {
                condition.add(rs.getString("condition"));
            }
            conditionsTable.getItems().setAll(condition);

            condition.add(0, "");
            diagnosisCondition.setItems(condition);
            diagnosisCondition.setValue("");
            diagnosisCondition.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((ObservableValue<? extends String> observable, String a, String b) -> getDiagnoses());
            newDiagnosisCondition.setItems(condition);
            newDiagnosisCondition.setValue("");


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

    private String idFromConsultant(String consultantName) throws SQLException {
        String sql = "SELECT id FROM Consultants WHERE consultant = '" + consultantName + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()) {
            return rs.getString("id");
        } else {
            return "NONE";
        }
    }

    private String idFromCondition(String conditionName) throws SQLException {
        String sql = "SELECT id FROM Conditions WHERE `condition` = '" + conditionName + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()) {
            return rs.getString("id");
        } else {
            return "NONE";
        }
    }

    private String idFromMedication(String medicationName) throws SQLException {
        String sql = "SELECT id FROM Medications WHERE medication = '" + medicationName + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()) {
            return rs.getString("id");
        } else {
            return "NONE";
        }
    }

    private String idFromLocation(String locationName) throws SQLException {
        String sql = "SELECT id FROM `GP Practices` WHERE location = '" + locationName + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()) {
            return rs.getString("id");
        } else {
            return "NONE";
        }
    }

    private String idFromTreatment(String treatmentName) throws SQLException {
        String sql = "SELECT id FROM Treatment WHERE treatment = '" + treatmentName + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()) {
            return rs.getString("id");
        } else {
            return "NONE";
        }
    }

    public void addPatient() {
        patientSPane.setExpanded(false);
        patientAddPane.setVisible(true);
    }

    public void addDiagnosis() {
        diagnosisSPane.setExpanded(false);
        diagnosisAddPane.setVisible(true);
    }

    public void addPrescription() {
        prescriptionSPane.setExpanded(false);
        prescriptionAddPane.setVisible(true);
    }

    public void addConsultation() {
        consultationSPane.setExpanded(false);
        consultationAddPane.setVisible(true);
    }

    public void addTreatment() {
        treatmentSPane.setExpanded(false);
        treatmentAddPane.setVisible(true);
    }

    public void cancelPatient() {
        patientAddPane.setVisible(false);
        newPatientFName.clear();
        newPatientLName.clear();
        newPatientAddr1.clear();
        newPatientAddr2.clear();
        newPatientCounty.clear();
        newPatientPCode.clear();
        newPatientNum.clear();
        newPatientNok.clear();
        newPatientNokNum.clear();
        newPatientRisk.setValue("");
        newPatientDOB.setValue(null);

        newPatientFName.getStyleClass().remove("required");
        newPatientLName.getStyleClass().remove("required");
        newPatientRisk.getStyleClass().remove("required");
        newPatientDOB.getStyleClass().remove("required");
    }

    public void cancelDiagnosis() {
        diagnosisAddPane.setVisible(false);
        newDiagnosisPID.setValue("");
        newDiagnosisPID.getStyleClass().remove("required");
        newDiagnosisCondition.setValue("");
        newDiagnosisCondition.getStyleClass().remove("required");
        newDiagnosisConsultant.setValue("");
        newDiagnosisConsultant.getStyleClass().remove("required");
        newDiagnosisDate.setValue(null);
        newDiagnosisComment.setText("");
    }

    public void cancelPrescription() {
        prescriptionAddPane.setVisible(false);
        newPrescriptionPID.setValue("");
        newPrescriptionPID.getStyleClass().remove("required");
        newPrescriptionMedication.setValue("");
        newPrescriptionMedication.getStyleClass().remove("required");
        newPrescriptionConsultant.setValue("");
        newPrescriptionConsultant.getStyleClass().remove("required");
        newPrescriptionDate.setValue(null);
        newPrescriptionComment.setText("");
    }

    public void cancelConsultation() {
        consultationAddPane.setVisible(false);
        newConsultationPID.setValue("");
        newConsultationPID.getStyleClass().remove("required");
        newConsultationLocation.setValue("");
        newConsultationLocation.getStyleClass().remove("required");
        newConsultationConsultant.setValue("");
        newConsultationConsultant.getStyleClass().remove("required");
        newConsultationDate.setValue(null);
        newConsultationDate.getStyleClass().remove("required");
        newConsultationComment.setText("");
        newConsultationHour.setValue("");
        newConsultationHour.getStyleClass().remove("required");
        newConsultationMinute.setValue("");
        newConsultationMinute.getStyleClass().remove("required");
    }

    public void cancelTreatment() {
        treatmentAddPane.setVisible(false);
        newTreatmentPID.setValue("");
        newTreatmentPID.getStyleClass().remove("required");
        newTreatmentTreatment.setValue("");
        newTreatmentTreatment.getStyleClass().remove("required");
        newTreatmentConsultant.setValue("");
        newTreatmentConsultant.getStyleClass().remove("required");
        newTreatmentDate.setValue(null);
        newTreatmentComment.setText("");
    }

    public void submitPatient() {
        String name = newPatientFName.getText();
        String surname = newPatientLName.getText();
        String addr1 = newPatientAddr1.getText();
        String addr2 = newPatientAddr2.getText();
        String county = newPatientCounty.getText();
        String pCode = newPatientPCode.getText();
        String num = newPatientNum.getText();
        String nok = newPatientNok.getText();
        String nokNum = newPatientNokNum.getText();
        String risk = newPatientRisk.getValue();
        LocalDate dobValue = newPatientDOB.getValue();

        if (!name.equals("") && !surname.equals("") && !risk.equals("") && (dobValue != null)) {
            String dob = dobValue.toString();


            String sql = "INSERT INTO Patients (first_name, surname, dob, address1, address2, county, postcode, contact_number, next_of_kin, kin_number, risk) " +
                    "VALUES ('" + name + "', '" + surname + "', '" + dob + "', '" + addr1 + "', '" + addr2 + "', '" + county + "', '" + pCode + "', '" + num + "', '" + nok + "', '" + nokNum + "', '" + risk + "')";
            try {
                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
                getPatients();
                cancelPatient();
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        } else {
            newPatientFName.getStyleClass().remove("required");
            newPatientLName.getStyleClass().remove("required");
            newPatientRisk.getStyleClass().remove("required");
            newPatientDOB.getStyleClass().remove("required");
            if (name.equals("")) {
                newPatientFName.getStyleClass().add("required");
            }
            if (surname.equals("")) {
                newPatientLName.getStyleClass().add("required");
            }
            if (risk.equals("")) {
                newPatientRisk.getStyleClass().add("required");
            }
            if (dobValue == null) {
                newPatientDOB.getStyleClass().add("required");
            }
        }
    }

    public void submitDiagnosis() {
        String id = newDiagnosisPID.getValue();
        String condition = newDiagnosisCondition.getValue();
        String consultant = newDiagnosisConsultant.getValue();
        String comment = newDiagnosisComment.getText();
        LocalDate localDate = newDiagnosisDate.getValue();

        if (!id.equals("") && !condition.equals("") && !consultant.equals("")) {
            try{
                id = id.split(":")[0];
                consultant = idFromConsultant(consultant);
                condition = idFromCondition(condition);

                String date;
                if (localDate == null) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date current = new Date();
                    date = dateFormat.format(current);
                } else {
                    date = localDate.toString();
                }

                String sql = "INSERT INTO Diagnoses (patient_id, `condition`, `date`, diagnostician, `comment`) " +
                        "VALUES ('" + id + "', '" + condition + "', '" + date + "', '" + consultant + "', '" + comment + "')";

                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
                getDiagnoses();
                cancelDiagnosis();
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        } else {
            newDiagnosisPID.getStyleClass().remove("required");
            newDiagnosisCondition.getStyleClass().remove("required");
            newDiagnosisConsultant.getStyleClass().remove("required");
            if (id.equals("")) {
                newDiagnosisPID.getStyleClass().add("required");
            }
            if (condition.equals("")) {
                newDiagnosisCondition.getStyleClass().add("required");
            }
            if (consultant.equals("")) {
                newDiagnosisConsultant.getStyleClass().add("required");
            }
        }
    }

    public void submitPrescription() {
        String id = newPrescriptionPID.getValue();
        String medication = newPrescriptionMedication.getValue();
        String consultant = newPrescriptionConsultant.getValue();
        String comment = newPrescriptionComment.getText();
        LocalDate localDate = newPrescriptionDate.getValue();

        if (!id.equals("") && !medication.equals("") && !consultant.equals("")) {
            try{
                id = id.split(":")[0];
                consultant = idFromConsultant(consultant);
                medication = idFromMedication(medication);

                String date;
                if (localDate == null) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date current = new Date();
                    date = dateFormat.format(current);
                } else {
                    date = localDate.toString();
                }

                String sql = "INSERT INTO Prescriptions (patient_id, medication, `date`, prescriber, `comment`) " +
                        "VALUES ('" + id + "', '" + medication + "', '" + date + "', '" + consultant + "', '" + comment + "')";

                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
                getPrescriptions();
                cancelPrescription();
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        } else {
            newPrescriptionPID.getStyleClass().remove("required");
            newPrescriptionMedication.getStyleClass().remove("required");
            newPrescriptionConsultant.getStyleClass().remove("required");
            if (id.equals("")) {
                newPrescriptionPID.getStyleClass().add("required");
            }
            if (medication.equals("")) {
                newPrescriptionMedication.getStyleClass().add("required");
            }
            if (consultant.equals("")) {
                newPrescriptionConsultant.getStyleClass().add("required");
            }
        }
    }

    public void submitConsultation() {
        String id = newConsultationPID.getValue();
        String location = newConsultationLocation.getValue();
        String consultant = newConsultationConsultant.getValue();
        String comment = newConsultationComment.getText();
        LocalDate localDate = newConsultationDate.getValue();
        String hour = newConsultationHour.getValue();
        String minute = newConsultationMinute.getValue();

        if (!id.equals("") && !location.equals("") && !consultant.equals("") && !hour.equals("") && !minute.equals("") && (localDate != null)) {
            try{
                id = id.split(":")[0];
                consultant = idFromConsultant(consultant);
                location = idFromLocation(location);
                String date = localDate.toString();
                String time = hour + ":" + minute + ":00";

                String sql = "INSERT INTO Consultations (patient_id, location, `date`, consultant, `time`, `comment`) " +
                        "VALUES ('" + id + "', '" + location + "', '" + date + "', '" + consultant + "', '" + time + "', '" + comment + "')";

                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
                getConsultations();
                cancelConsultation();
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        } else {
            newConsultationPID.getStyleClass().remove("required");
            newConsultationLocation.getStyleClass().remove("required");
            newConsultationConsultant.getStyleClass().remove("required");
            newConsultationDate.getStyleClass().remove("required");
            newConsultationHour.getStyleClass().remove("required");
            newConsultationMinute.getStyleClass().remove("required");
            if (id.equals("")) {
                newConsultationPID.getStyleClass().add("required");
            }
            if (location.equals("")) {
                newConsultationLocation.getStyleClass().add("required");
            }
            if (consultant.equals("")) {
                newConsultationConsultant.getStyleClass().add("required");
            }
            if (hour.equals("")) {
                newConsultationHour.getStyleClass().add("required");
            }
            if (minute.equals("")) {
                newConsultationMinute.getStyleClass().add("required");
            }
            if (localDate == null) {
                newConsultationDate.getStyleClass().add("required");
            }
        }
    }

    public void submitTreatment() {
        String id = newTreatmentPID.getValue();
        String treatment = newTreatmentTreatment.getValue();
        String consultant = newTreatmentConsultant.getValue();
        String comment = newTreatmentComment.getText();
        LocalDate localDate = newTreatmentDate.getValue();

        if (!id.equals("") && !treatment.equals("") && !consultant.equals("")) {
            try{
                id = id.split(":")[0];
                consultant = idFromConsultant(consultant);
                treatment = idFromTreatment(treatment);

                String date;
                if (localDate == null) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date current = new Date();
                    date = dateFormat.format(current);
                } else {
                    date = localDate.toString();
                }

                String sql = "INSERT INTO Treatments (patient_id, treatment, date_started, consultant, `comment`) " +
                        "VALUES ('" + id + "', '" + treatment + "', '" + date + "', '" + consultant + "', '" + comment + "')";

                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
                getTreatments();
                cancelTreatment();
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        } else {
            newTreatmentPID.getStyleClass().remove("required");
            newTreatmentTreatment.getStyleClass().remove("required");
            newTreatmentConsultant.getStyleClass().remove("required");
            if (id.equals("")) {
                newTreatmentPID.getStyleClass().add("required");
            }
            if (treatment.equals("")) {
                newTreatmentTreatment.getStyleClass().add("required");
            }
            if (consultant.equals("")) {
                newTreatmentConsultant.getStyleClass().add("required");
            }
        }
    }

    public void addCondition() {
        String condition = newCondition.getText();
        if (!condition.equals("")) {
            String sql = "INSERT INTO Conditions (`condition`) VALUES ('" + condition + "')";
            try {
                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        }
        updateConditionSelectors();
        newCondition.clear();
    }

    public void addMedication() {
        String medication = newMedication.getText();
        if (!medication.equals("")) {
            String sql = "INSERT INTO Medications (`medication`) VALUES ('" + medication + "')";
            try {
                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        }
        updateMedicationSelectors();
        newMedication.clear();
    }

    public void addConsultant() {
        String consultant = newConsultant.getText();
        if (!consultant.equals("")) {
            String sql = "INSERT INTO Consultants (`consultant`) VALUES ('" + consultant + "')";
            try {
                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        }
        updateConsultantSelectors();
        newConsultant.clear();
    }

    public void addLocation() {
        String location = newLocation.getText();
        if (!location.equals("")) {
            String sql = "INSERT INTO `GP Practices` (`location`) VALUES ('" + location + "')";
            try {
                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        }
        updateLocationSelectors();
        newLocation.clear();
    }

    public void addTreating() {
        String treatment = newTreatment.getText();
        if (!treatment.equals("")) {
            String sql = "INSERT INTO Treatment (`treatment`) VALUES ('" + treatment + "')";
            try {
                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        }
        updateTreatmentSelectors();
        newTreatment.clear();
    }

}