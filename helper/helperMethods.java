package helper;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**This class contains more generic methods that help with classes across the application. */
public class helperMethods {

    private static Stage stage;
    private static Parent scene;

    /**Checks to see if the local language is French.
     * @return boolean*/
    public static boolean isInFrench() {
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("fr")) {
        }
        return true;
    }

    /**Translates words and phrases to the user's system language.
     * English and French translations are the 2 options for this translation method.
     * Utilizes the ResourceBundle "Nat" included in the helper package.
     * @param keyPhrase the phrase that needs to be translated. */
    public static String translateToLocale(String keyPhrase){

        ResourceBundle rb = ResourceBundle.getBundle("helper/Nat", Locale.getDefault());
        String outputPhrase = rb.getString(keyPhrase);
        return outputPhrase;
    }

    /**Sets a label's text to the language of the user's system.
     * @param label the label text needing to be changed.
     * @param text the properly translated text. */
    public static void setTextTo (Label label, String text){
        label.setText(helper.helperMethods.translateToLocale(text));
    }

    /**Sets a button's text to the language of the user's system.
     * @param button the button text needing to be translated.
     * @param text the properly translated text. */
    public static void setBtnTextTo (Button button, String text){
        button.setText(helper.helperMethods.translateToLocale(text));
    }

    /**Utilizes ActionEvents to transfer to the next desired GUI screen.
     * @param event the button press.
     * @param screen the next screen displayed after the button press. */
    public static void goToNextScreen(ActionEvent event, String screen) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(helper.helperMethods.class.getResource(screen));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**Displays an Error message to the user.
     * @param content the phrase the error message should display to the user. */
    public static void displayAlert(String content){
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**Displays a confirmation alert to the user to receive assurance of wanting to navigate to next GUI screen.
     * @param event the button press that prompts the alert.
     * @param exitAlert the message displayed to the user.
     * @param screen the next screen of the GUI navigated to if confirmation button "OK" is selected. */
    public static void confirmationAlert (String exitAlert, ActionEvent event, String screen) throws IOException {
        var alert = new Alert(Alert.AlertType.CONFIRMATION, exitAlert);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            goToNextScreen(event, screen);
        }
    }

    /**Displays a confirmation alert to user to receive assurance of wanting to exit the application.
     * @param exitAlert the message displayed to the user. */
    public static void exitAlert (String exitAlert){
        var alert = new Alert(Alert.AlertType.CONFIRMATION, exitAlert);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**Displays an alert to notify the user if they do/do not have an appointment within 15 minutes of login.
     * @param userId the user's ID to check the database for corresponding appointments
     * @param loginTime the time of user's login
     * @return a result set of appointments that start within 15 minutes of login. */
    public static ResultSet hasUpcomingAppointment(int userId, Timestamp loginTime) throws SQLException {
        LocalDateTime endRange = loginTime.toLocalDateTime().plusMinutes(15);
        Timestamp endRangeTimestamp = Timestamp.valueOf(endRange);
        String sql = "SELECT * FROM APPOINTMENTS WHERE User_ID = ? AND Start BETWEEN ? AND ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setTimestamp(2, loginTime);
        ps.setTimestamp(3, endRangeTimestamp);
        ResultSet rs = ps.executeQuery();
        return rs;
    }

    /**A method to check the user's login credentials.
     * Also writes the username and timestamp to a .txt file, and utilizes the hasUpcomingAppointment method to display correct message to user.
     * @param userName username
     * @param password password
     * @param event the login button press. */
    public static void checkLogin(String userName, String password, ActionEvent event){
        try{
            FileWriter fileWriter = new FileWriter("src/login_activity.txt", true);
            LocalDateTime now = LocalDateTime.now();
            String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String status = "SUCCESS";

            String sql = "SELECT USERS.User_ID, APPOINTMENTS.Appointment_ID, APPOINTMENTS.Start FROM USERS JOIN APPOINTMENTS ON USERS.User_ID = APPOINTMENTS.User_ID WHERE USERS.User_Name = ? AND USERS.Password = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(helper.helperMethods.class.getResource("/view/CustomerPage.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                fileWriter.append(userName + "," + timestamp + "," + status + "\n");

                int userId = rs.getInt("User_ID");

                Timestamp loginTimestamp = Timestamp.valueOf(now);
                ResultSet rs2 = hasUpcomingAppointment(userId, loginTimestamp);

                if (rs2.next()) {

                    int appointmentId = rs2.getInt("Appointment_ID");
                    Timestamp start = rs2.getTimestamp("Start");
                    LocalDateTime startDateTime = start.toLocalDateTime();
                    String formattedStart = startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    var alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText(userName + " has an upcoming appointment within 15 minutes. It has ID " + appointmentId + " and starts at " + formattedStart);
                    alert.showAndWait();

                } else if (!rs2.next()) {
                    var alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText(userName + " has no appointments within 15 minutes of now.");
                    alert.showAndWait();
                }

            }
            else{
                if(isInFrench()){
                    String loginAlert = translateToLocale("loginAlert");
                    displayAlert(loginAlert);
                    status = "FAILED";
                    fileWriter.append(userName + "," + timestamp + "," + status + "\n");
                }
                else{
                    displayAlert("Username or password incorrect. Please try again.");
                    status = "FAILED";
                    fileWriter.append(userName + "," + timestamp + "," + status + "\n");
                }
            }
            fileWriter.close();
        } catch(SQLException e){

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
