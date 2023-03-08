package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.AppointmentData;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**This class is the controller for the report to display appointments by month and type. */
public class CustomerMonthReport implements Initializable {

    /**TableView, columns and a button to exit back to reports. */
    @FXML private TableColumn<?, ?> monthCol;
    @FXML private TableColumn<?, ?> numberCol;
    @FXML private TableView<AppointmentData> reportTbl;
    @FXML private TableColumn<?, ?> typeCol;
    @FXML private Button backBtn;

    /**Exits back to the reports page. */
    @FXML void onActBack(ActionEvent event) throws IOException {

        helper.helperMethods.goToNextScreen(event, "/view/ReportsView.fxml");

    }

    /**Initializes the controller to set the TableView to display appointments by month and type with a count of each. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            reportTbl.setItems(model.AppointmentData.getAllApptData());
            monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            numberCol.setCellValueFactory(new PropertyValueFactory<>("count"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
