package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

/**This class is the controller for the GUI displaying the report choices. */
public class Reports {

    /**Visual GUI variables including buttons for navigation. */
    @FXML private Button contactBtn;
    @FXML private Button customerBtn;
    @FXML private Button loginBtn;
    @FXML private Button quitBtn;
    @FXML private Button viewApptsBtn;
    @FXML private Button viewCustomersBtn;

    /**Transitions to Contact Schedule report view. */
    @FXML void onActContactBtn(ActionEvent event) throws IOException {

        helper.helperMethods.goToNextScreen(event, "/view/ContactSchedules.fxml");
    }

    /**Transitions to the report for appointments by type and month. */
    @FXML void onActCustomerBtn(ActionEvent event) throws IOException {

        helper.helperMethods.goToNextScreen(event, "/view/CustomerMonthReport.fxml");
    }

    /**Transitions to the report for the login attempts. */
    @FXML void onActLoginBtn(ActionEvent event) throws IOException {

        helper.helperMethods.goToNextScreen(event, "/view/LoginAttempts.fxml");
    }

    /**Exits out of application. */
    @FXML void onActQuit(ActionEvent event) {

        helper.helperMethods.exitAlert("Are you sure you want to exit the application?");

    }

    /**Returns to the Appointments page. */
    @FXML void onActViewAppts(ActionEvent event) throws IOException {

        helper.helperMethods.confirmationAlert("Are you sure you want to return to the appointments page?", event, "/view/Appointments.fxml");

    }

    /**Returns to the Customers page. */
    @FXML void onActViewCustomers(ActionEvent event) throws IOException {

        helper.helperMethods.confirmationAlert("Are you sure you want to return to the customer page?", event, "/view/CustomerPage.fxml");

    }

}
