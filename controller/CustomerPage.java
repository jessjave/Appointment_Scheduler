package controller;

import helper.CustomerQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Countries;
import model.Customer;
import model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static helper.CustomerQuery.clearAllCustomers;
import static helper.CustomerQuery.getAllCustomers;

/**This is the controller class for the customer page.
 * It includes a populated TableView, buttons, labels, combo boxes and text fields for user information input.
 * A lambda expression is used in this class in the initialize method. */
public class CustomerPage implements Initializable {

    /**Visual aspects of the GUI including a TableView, buttons, labels, text fields and combo boxes. */
    @FXML private Button addBtn;
    @FXML private Label addCustomerLbl;
    @FXML private TableColumn<?, ?> addressCol;
    @FXML private TextField addressTxt;
    @FXML private Button apptBtn;
    @FXML private ComboBox<Countries> countryCombo;
    @FXML private TableView<Customer> customerTbl;
    @FXML private Button deleteBtn;
    @FXML private TableColumn<?, ?> divisionIdCol;
    @FXML private Button exitBtn;
    @FXML private TableColumn<?, ?> idCol;
    @FXML private TextField idTxt;
    @FXML private TableColumn<?, ?> nameCol;
    @FXML private TextField nameTxt;
    @FXML private TableColumn<?, ?> phoneCol;
    @FXML private TextField phoneTxt;
    @FXML private TableColumn<?, ?> postCodeCol;
    @FXML private TextField postalTxt;
    @FXML private Button saveBtn;
    @FXML private ComboBox<FirstLevelDivision> stateCombo;
    @FXML private Button updateBtn;
    @FXML private Button reportsBtn;

    /**Clears text fields and makes them editable for data input. */
    @FXML void onActAddBtn(ActionEvent event) {
        addCustomerLbl.setText("Add Customer");

        stateCombo.setItems(FirstLevelDivision.getAllFLDs());
        countryCombo.setItems(Countries.getAllCountries());

        customerTbl.getSelectionModel().clearSelection();

        idTxt.setText("auto-generated");
        nameTxt.setDisable(false);
        addressTxt.setDisable(false);
        postalTxt.setDisable(false);
        phoneTxt.setDisable(false);
        nameTxt.clear();
        addressTxt.clear();
        postalTxt.clear();
        phoneTxt.clear();

    }

    /**Deletes a customer from the TableView and database. */
    @FXML void onActDeleteBtn(ActionEvent event) throws SQLException {
        CustomerQuery cq = new CustomerQuery();
        Customer selectedCustomer = (Customer) customerTbl.getSelectionModel().getSelectedItem();

        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer, and all their related appointments?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            CustomerQuery.delete(selectedCustomer);
            customerTbl.setItems(cq.getAllCustomers());
        }
    }

    /**Exits out of application with confirmation alert. */
    @FXML void onActExitBtn(ActionEvent event) {
        String exitAlert = helper.helperMethods.translateToLocale("exitAlert");
        helper.helperMethods.exitAlert(exitAlert);
    }

    /**Transitions to the reports page. */
    @FXML void onActReports(ActionEvent event) throws IOException {
        helper.helperMethods.goToNextScreen(event, "/view/ReportsView.fxml");
    }

    /**Populates text fields and combo boxes with the information to be updates from the selected customer. */
    @FXML void onActUpdateBtn(ActionEvent event) {
        try {
            addCustomerLbl.setText("Update Customer");

            nameTxt.setDisable(false);
            addressTxt.setDisable(false);
            postalTxt.setDisable(false);
            phoneTxt.setDisable(false);

            stateCombo.setItems(FirstLevelDivision.getAllFLDs());
            countryCombo.setItems(Countries.getAllCountries());
            FirstLevelDivision myFirstLevelDivision = null;
            Countries myCountry = null;

            Customer selectedCustomer = (Customer) customerTbl.getSelectionModel().getSelectedItem();

            idTxt.setText(String.valueOf(selectedCustomer.getCustomerId()));
            nameTxt.setText(selectedCustomer.getCustomerName());
            addressTxt.setText(selectedCustomer.getCustomerAddress());
            postalTxt.setText(selectedCustomer.getCustomerPostalCode());
            phoneTxt.setText(selectedCustomer.getCustomerPhone());

            for (FirstLevelDivision fld : stateCombo.getItems()) {
                if (selectedCustomer.getDivisionId() == fld.getDivisionId()) {
                    myFirstLevelDivision = fld;


                    for (Countries country : Countries.getAllCountries()) {
                        if (myFirstLevelDivision.getCountryId() == country.getCountryId())
                            myCountry = country;

                    }
                }
            }
            countryCombo.setValue(myCountry);
            stateCombo.setValue(myFirstLevelDivision);
        }
        catch (NullPointerException e){
            helper.helperMethods.displayAlert("Please choose a customer to update.");
        }
    }

    /**Transitions to the Appointments page. */
    @FXML void onActApptBtn(ActionEvent event) throws IOException {

        customerTbl.getItems().clear();
        helper.helperMethods.goToNextScreen(event, "/view/Appointments.fxml");

    }

    /**Saves an updated or new customer object to the TableView and database with data validation and error handling. */
    @FXML void onActSaveBtn(ActionEvent event) throws SQLException {
        try {
            CustomerQuery cq = new CustomerQuery();
            Customer selectedCustomer = (Customer) customerTbl.getSelectionModel().getSelectedItem();

            customerTbl.getItems().clear();

            String customerName = nameTxt.getText();
            String customerAddress = addressTxt.getText();
            String customerPostal = postalTxt.getText();
            String customerPhone = phoneTxt.getText();
            int divisionId = stateCombo.getValue().getDivisionId();
            String pattern = "^[\\d-]+$";
            ObservableList allCustomers = null;

            cq.ifStringEmpty(customerName, "Name");
            cq.ifStringEmpty(customerAddress, "Address");
            cq.ifStringEmpty(customerPostal, "Postal Code");
            cq.ifStringEmpty(customerPhone, "Phone Number");

            if(!customerPhone.matches(pattern)){
                helper.helperMethods.displayAlert("Please enter valid value for Phone Number.");
            }

            if (selectedCustomer == null) {
                cq.insert(customerName, customerAddress, customerPostal, customerPhone, divisionId);
                allCustomers = cq.getAllCustomers();
                customerTbl.setItems(allCustomers);

            } else {
                int customerId = Integer.parseInt(idTxt.getText());
                cq.update(customerName, customerAddress, customerPostal, customerPhone, divisionId, customerId);
                allCustomers = cq.getAllCustomers();
                customerTbl.setItems(allCustomers);
            }
        } catch (NullPointerException e){
            helper.helperMethods.displayAlert("Please make appropriate selection for Country and State/Province");
            customerTbl.setItems(getAllCustomers());
        }


    }

    /**Initializes the controller for the Customer page of the GUI.
     * A lambda expression is used to concisely and effectively define the behavior of a listener that updates the options of the State/Province combo box based on the selected country.
     * This method also populates the TableView with all customer objects from the database. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustomerQuery cq = new CustomerQuery();
        try {

            stateCombo.setItems(FirstLevelDivision.getAllFLDs());
            countryCombo.setItems(Countries.getAllCountries());

            ObservableList selectedStates = FXCollections.observableArrayList();

            //lambda #2
            countryCombo.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                stateCombo.setItems(null);
            if (newValue != null){
                selectedStates.clear();
                for (int i = 0; i < FirstLevelDivision.getAllFLDs().size(); i++) {
                    FirstLevelDivision fld = FirstLevelDivision.getAllFLDs().get(i);
                    if(fld.getCountryId() == newValue.getCountryId()){
                        selectedStates.add(fld);

                    }
                }
            }
                stateCombo.setItems(selectedStates);
        });

            clearAllCustomers();
            customerTbl.setItems(cq.getAllCustomers());
            idCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            postCodeCol.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
            phoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
            divisionIdCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
