package controller;

import javafx.fxml.Initializable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**This is controller to display the 3rd report based off the .txt file.
 * It displays a report of the monthly, weekly, and daily login attempts along with the successes and failures.*/
public class LoginAttempts implements Initializable {

    /**Variables for report*/
    int successToday = 0;
    int successThisWeek = 0;
    int successThisMonth = 0;
    int failureToday = 0;
    int failureThisWeek = 0;
    int failureThisMonth = 0;
    LocalDate today = LocalDate.now();
    LocalDate weekStart = today.with(DayOfWeek.MONDAY);
    LocalDate monthStart = today.withDayOfMonth(1);

    /**Labels set to change based off of report findings*/
    @FXML private Label monthFail;
    @FXML private Label monthSuccess;
    @FXML private Button returnBtn;
    @FXML private Label todayFail;
    @FXML private Label todaySuccess;
    @FXML private Label weekFail;
    @FXML private Label weekSuccess;

    /**Returns back to reports page. */
    @FXML void onActReturn(ActionEvent event) throws IOException {

        helper.helperMethods.confirmationAlert("Are you sure you want to return to Reports?", event, "/view/ReportsView.fxml");
    }

    /**Initializes controller for the Login Reports page.
     * This method uses the FileReader and BufferedReader classes to parse through the login_activity.txt file and display a report of all the successful and failed logins for the day, month and week. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FileReader fileReader = new FileReader("src/login_activity.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                String userName = parts[0];
                String timestamp = parts[1];
                String status = parts[2];
                LocalDateTime loginTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDate loginDate = loginTime.toLocalDate();
                if (status.equals("SUCCESS")) {
                    if (loginDate.equals(today)) {
                        successToday++;
                    }
                    if (loginDate.isAfter(weekStart) || loginDate.isEqual(weekStart)) {
                        successThisWeek++;
                    }
                    if (loginDate.isAfter(monthStart) || loginDate.isEqual(monthStart)) {
                        successThisMonth++;
                    }
                } else if (status.equals("FAILED")) {
                    if (loginDate.equals(today)) {
                        failureToday++;
                    }
                    if (loginDate.isAfter(weekStart) || loginDate.isEqual(weekStart)) {
                        failureThisWeek++;
                    }
                    if (loginDate.isAfter(monthStart) || loginDate.isEqual(monthStart)) {
                        failureThisMonth++;
                    }
                }
            }
            todaySuccess.setText(String.valueOf(successToday));
            weekSuccess.setText(String.valueOf(successThisWeek));
            monthSuccess.setText(String.valueOf(successThisMonth));
            todayFail.setText(String.valueOf(failureToday));
            weekFail.setText(String.valueOf(failureThisWeek));
            monthFail.setText(String.valueOf(failureThisMonth));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
