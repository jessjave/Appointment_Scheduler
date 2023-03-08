package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Contacts;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**This class is the controller for the report page associated with contacts and their designated appointments*/
public class ContactSchedules implements Initializable {

    /**Variables for the TableView and columns*/
    @FXML private TableColumn<?, ?> apptIdCol;
    @FXML private TableColumn<?, ?> customerIdCol;
    @FXML private TableColumn<?, ?> descriptionCol;
    @FXML private TableColumn<?, ?> endCol;
    @FXML private TableColumn<?, ?> nameCol;
    @FXML private TableView<Contacts> scheduleTbl;
    @FXML private TableColumn<?, ?> startCol;
    @FXML private TableColumn<?, ?> titleCol;
    @FXML private TableColumn<?, ?> typeCol;

    /**Button to exit back to reports page*/
    @FXML private Button exitBtn;

    /**Exits back to the reports page. */
    @FXML void onActExitBtn(ActionEvent event) throws IOException {

        helper.helperMethods.confirmationAlert("Are you sure you want to return to Reports?", event, "/view/ReportsView.fxml");

    }

    /**Initializes the controller for the Contact Schedules view.
     * Sets the TableView to display contacts and their appointments in order to provide a visual of their schedules. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            scheduleTbl.setItems(model.Contacts.getAllContactsForReport());

            apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
