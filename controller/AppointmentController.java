package controller;

import helper.CustomerQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import static helper.CustomerQuery.clearAllCustomers;

/**This class is the controller for the Appointments page of the application.
 * A lambda expression is used in the initialize method of this class. */
public class AppointmentController implements Initializable {

    /**All variables for the GUI. Includes a TableView and its columns, buttons, labels and text fields for information input. */
    @FXML private RadioButton allRBtn1;
    @FXML private TextField apptIdTxt;
    @FXML private TableView<model.Appointments> apptTbl;
    @FXML private ToggleGroup apptViewTG;
    @FXML private TableColumn<?, ?> contactCol;
    @FXML private ComboBox<Contacts> contactCombo;
    @FXML private TableColumn<?, ?> customerCol;
    @FXML private ComboBox<Customer> customerCombo;
    @FXML private Button cancelBtn;
    @FXML private Button deleteBtn;
    @FXML private TableColumn<?, ?> descriptionCol;
    @FXML private TextField descriptionTxt;
    @FXML private TableColumn<?, ?> endCol;
    @FXML private ComboBox<LocalTime> endDateTime;
    @FXML private TableColumn<?, ?> idCol;
    @FXML private Label infoLbl;
    @FXML private TableColumn<?, ?> locationCol;
    @FXML private TextField locationTxt;
    @FXML private RadioButton monthlyRBtn;
    @FXML private Button newBtn;
    @FXML private Button saveBtn;
    @FXML private TableColumn<?, ?> startCol;
    @FXML private DatePicker startDatePick;
    @FXML private ComboBox<LocalTime> startTimeCombo;
    @FXML private Label timeZoneLbl;
    @FXML private TableColumn<?, ?> titleCol;
    @FXML private TextField titleTxt;
    @FXML private TableColumn<?, ?> typeCol;
    @FXML private TextField typeTxt;
    @FXML private Button updateBtn;
    @FXML private TableColumn<?, ?> userCol;
    @FXML private ComboBox<Users> userCombo;
    @FXML private RadioButton weeklyRBtn;
    @FXML private Button reportsBtn;
    /**Formats date to a more readable form for the TableView. */
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**Method for when the "All Appointments" radio button is selected. */
    @FXML void onActAllRBtn(ActionEvent event) throws SQLException {

    }
    /**Method to transition to the "Reports" page of the GUI. */
    @FXML void onActViewReports(ActionEvent event) throws IOException {

        helper.helperMethods.goToNextScreen(event, "/view/ReportsView.fxml");
    }
    /**Returns the user to the customer page.
     * @return confirmation alert*/
    @FXML void onActCancelBtn(ActionEvent event) throws IOException {

        helper.helperMethods.confirmationAlert("Are you sure you want to return to the customer page?", event, "/view/CustomerPage.fxml");
    }
    /**Deletes an appointment from the TableView and database.
     * Returns a confirmation alert and error handling.*/
    @FXML void onActDeleteBtn(ActionEvent event) throws SQLException {
        try {
            model.Appointments selectedAppointment = apptTbl.getSelectionModel().getSelectedItem();
            String type = selectedAppointment.getType();
            int id = selectedAppointment.getApptId();

            var alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?\n" +
                    "It is a(n) " + type + " appointment type with ID " + id + ".");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                model.Appointments.delete(selectedAppointment);
                apptTbl.setItems(model.Appointments.getAllApptsForTable());
            }
        } catch (NullPointerException e){
            helper.helperMethods.displayAlert("Please select an appointment you wish to delete");
        }
    }
    /**Controls the "Monthly Appointments" radio button*/
    @FXML void onActMonthlyRBtn(ActionEvent event) {
        monthlyRBtn.setToggleGroup(apptViewTG);
    }

    /**Allows the user to enter information to create a new appointment.
     * Also clears the text fields potentially filled when performing other functions.*/
    @FXML void onActNewBtn(ActionEvent event) throws SQLException {
        infoLbl.setText("Add Appointment");

        apptTbl.getSelectionModel().clearSelection();

        apptIdTxt.setText("auto-generated");
        titleTxt.setDisable(false);
        descriptionTxt.setDisable(false);
        locationTxt.setDisable(false);
        typeTxt.setDisable(false);
        titleTxt.clear();
        descriptionTxt.clear();
        locationTxt.clear();
        typeTxt.clear();
    }

    /**Saves a new or updated appointment to the database.
     * Takes the input for all relevant text fields and combo boxes to input relevant information.
     * Converts the time information to UTC time from the user's system time for proper data collection.
     * Then clears all input fields in preparation for the next function performed by the user.
     * @return either updated or new appointment object*/
    @FXML void onActSaveBtn(ActionEvent event) throws SQLException {
        try {
            Appointments selectedAppt = (Appointments) apptTbl.getSelectionModel().getSelectedItem();

            String title = titleTxt.getText();
            String description = descriptionTxt.getText();
            String location = locationTxt.getText();
            String type = typeTxt.getText();

            int contactId = contactCombo.getValue().getContactId();
            int customerId = customerCombo.getValue().getCustomerId();
            int userId = userCombo.getValue().getUserId();

            CustomerQuery.ifStringEmpty(title, "Title");
            CustomerQuery.ifStringEmpty(description, "Description");
            CustomerQuery.ifStringEmpty(location, "Location");
            CustomerQuery.ifStringEmpty(type, "Type");

            LocalDate localDate = LocalDate.of(startDatePick.getValue().getYear(), startDatePick.getValue().getMonth(), startDatePick.getValue().getDayOfMonth());
            LocalTime localTimeStart = LocalTime.of(startTimeCombo.getValue().getHour(), startTimeCombo.getValue().getMinute(), startTimeCombo.getValue().getSecond());
            LocalTime localTimeEnd = LocalTime.of(endDateTime.getValue().getHour(), endDateTime.getValue().getMinute(), endDateTime.getValue().getSecond());

            if (localTimeEnd.isBefore(localTimeStart)) {
                helper.helperMethods.displayAlert("Please select a starting appointment time that is before the end of the appointment time.");
            }

            LocalDateTime localDateTimeStart = LocalDateTime.of(localDate, localTimeStart);
            LocalDateTime localDateTimeEnd = LocalDateTime.of(localDate, localTimeEnd);

            Instant instantStart = localDateTimeStart.toInstant(ZoneOffset.UTC);
            Instant instantEnd = localDateTimeEnd.toInstant(ZoneOffset.UTC);

            LocalDateTime zoneStart = LocalDateTime.ofInstant(instantStart, ZoneOffset.UTC);
            LocalDateTime zoneEnd = LocalDateTime.ofInstant(instantEnd, ZoneOffset.UTC);


            Timestamp start = Timestamp.valueOf(zoneStart);
            Timestamp end = Timestamp.valueOf(zoneEnd);

            ObservableList allAppts = null;

            if (selectedAppt == null) {
                Appointments.insert(title, description, location, type, customerId, userId, contactId, start, end);
                allAppts = Appointments.getAllApptsForTable();
                apptTbl.setItems(allAppts);

            } else {
                int apptId = Integer.parseInt(apptIdTxt.getText());
                Appointments.update(title, description, location, type, customerId, userId, contactId, apptId, start, end);
                allAppts = Appointments.getAllApptsForTable();
                apptTbl.setItems(allAppts);
            }

            titleTxt.clear();
            descriptionTxt.clear();
            locationTxt.clear();
            typeTxt.clear();
            apptIdTxt.clear();
            titleTxt.setDisable(true);
            descriptionTxt.setDisable(true);
            locationTxt.setDisable(true);
            typeTxt.setDisable(true);
        }catch (NullPointerException e){
            helper.helperMethods.displayAlert("Please ensure the date and both time fields are selected.");
        }

    }
    /**Populates the text fields and combo boxes for the selected appointment for update. */
    @FXML void onActUpdateBtn(ActionEvent event) throws SQLException {
        try {
            infoLbl.setText("Update Appointment");
            titleTxt.setDisable(false);
            descriptionTxt.setDisable(false);
            locationTxt.setDisable(false);
            typeTxt.setDisable(false);

            contactCombo.setItems(Contacts.getAllContacts());
            customerCombo.setItems(CustomerQuery.getAllCustomers());
            userCombo.setItems(Users.getAllUsers());
            Contacts myContact = null;
            Customer myCustomer = null;
            Users myUser = null;

            Appointments selectedAppt = (Appointments) apptTbl.getSelectionModel().getSelectedItem();

            apptIdTxt.setText(String.valueOf(selectedAppt.getApptId()));
            titleTxt.setText(selectedAppt.getTitle());
            descriptionTxt.setText(selectedAppt.getDescription());
            locationTxt.setText(selectedAppt.getLocation());
            typeTxt.setText(selectedAppt.getType());

            String dateString = String.valueOf(selectedAppt.getStartD());
            String endString = String.valueOf(selectedAppt.getEndD());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
            LocalDateTime endDTime = LocalDateTime.parse(endString, formatter);
            LocalDate date = dateTime.toLocalDate();
            LocalTime startTime = dateTime.toLocalTime();
            LocalTime endTime = endDTime.toLocalTime();

            startDatePick.setValue(date);
            startTimeCombo.setValue(startTime);
            endDateTime.setValue(endTime);


            for (Contacts contact : contactCombo.getItems()) {
                if (selectedAppt.getContactId() == contact.getContactId()) {
                    myContact = contact;

                    for (Customer customer : customerCombo.getItems()) {
                        if (selectedAppt.getCustomerId() == customer.getCustomerId())
                            myCustomer = customer;

                        for (Users user : userCombo.getItems()) {
                            if (selectedAppt.getUserId() == user.getUserId())
                                myUser = user;
                        }
                    }
                }
            }
            contactCombo.setValue(myContact);
            customerCombo.setValue(myCustomer);
            userCombo.setValue(myUser);
        }
        catch (NullPointerException e){
            helper.helperMethods.displayAlert("Please select an appointment to update.");
        }

    }
    /**Controls the weekly appointments radio button. */
    @FXML void onActWeeklyRBtn(ActionEvent event) throws SQLException {
    }

    /**This method initializes the controller.
     * It includes population of the TableView and combo boxes.
     * A lambda is used in the expression that filters the TableView utilizing the radio buttons to display this week's, month's and all appointments.
     * The lambda is used to provide a concise and readable way to define the filtering criteria for the appointments based on the selected radio button. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            //Setting all items in the TableView and ComboBoxes
            apptTbl.setItems(model.Appointments.getAllApptsForTable());
            contactCombo.setItems(Contacts.getAllContacts());
            clearAllCustomers();
            customerCombo.setItems(CustomerQuery.getAllCustomers());
            userCombo.setItems(Users.getAllUsers());

            idCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("startD"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("endD"));
            customerCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            userCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
            contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));

            timeZoneLbl.setText(ZoneId.systemDefault().toString());

            LocalTime start = LocalTime.of(6, 0);
            LocalTime end = LocalTime.of(20,0);

            while(start.isBefore(end.plusSeconds(1))){
                startTimeCombo.getItems().add(start);
                endDateTime.getItems().add(start);
                start = start.plusMinutes(10);
            }

          //Filtering the TableView by this month's and this week's appointments
        ObservableList<Appointments> allAppts = null;
            allAppts = FXCollections.observableArrayList(Appointments.getAllApptsForTable());

        FilteredList<Appointments> filteredAppts = new FilteredList<>(allAppts);

        weeklyRBtn.setToggleGroup(apptViewTG);
        monthlyRBtn.setToggleGroup(apptViewTG);

        //lambda #1
        apptViewTG.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (weeklyRBtn.isSelected()) {
                LocalDate now = LocalDate.now();
                LocalDate startOfWeek = now.with(DayOfWeek.MONDAY);
                LocalDate endOfWeek = now.with(DayOfWeek.SUNDAY);
                filteredAppts.setPredicate(appt -> {
                    LocalDateTime startD = LocalDateTime.parse(appt.getStartD(), formatter);
                    return startD.isAfter(startOfWeek.atStartOfDay()) && startD.isBefore(endOfWeek.atTime(23, 59, 59));
                });
            }else if (monthlyRBtn.isSelected()) {
                    LocalDate now = LocalDate.now();
                    LocalDate startOfMonth = now.withDayOfMonth(1);
                    LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());
                    filteredAppts.setPredicate(appt -> {
                        LocalDateTime startD = LocalDateTime.parse(appt.getStartD(), formatter);
                        return startD.isAfter(startOfMonth.atStartOfDay()) && startD.isBefore(endOfMonth.atTime(23, 59, 59));
                    });
                } else {
                    filteredAppts.setPredicate(null);
                }
            });

            apptTbl.setItems(filteredAppts);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
