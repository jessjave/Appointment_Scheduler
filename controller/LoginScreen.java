package controller;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ResourceBundle;

/**This is the controller class for the Login Screen. */
public class LoginScreen implements Initializable {

    /**Visual GUI aspects including labels, text fields and buttons. */
    @FXML private Label loginLbl;
    @FXML private Label userNameLbl;
    @FXML private TextField usernameTxt;
    @FXML private Label passwordLbl;
    @FXML private TextField passwordTxt;
    @FXML private Label locationLbl;
    @FXML private Label zoneIdLbl;
    @FXML private Button loginBtn;
    @FXML private Button exitBtn;

    /**Exits out of application. */
    @FXML void onActExit(ActionEvent event) {

        String exitAlert = helper.helperMethods.translateToLocale("exitAlert");
        helper.helperMethods.exitAlert(exitAlert);
    }

    /**Utilizes a helper method to check the login and record the attempt in login_activity.txt*/
    @FXML void onActLogin(ActionEvent event) throws IOException, SQLException {
        try {
            helper.helperMethods.checkLogin(usernameTxt.getText(), passwordTxt.getText(), event);
        }
        catch (NullPointerException e) {
            if (helper.helperMethods.isInFrench()) {
                String loginAlert = helper.helperMethods.translateToLocale("loginAlert");
                helper.helperMethods.displayAlert(loginAlert);
            } else {
                helper.helperMethods.displayAlert("Username or password incorrect. Please try again.");
            }
        }
    }

    /**Initializes controller and defines parameters for labels if user's language is set to French. */
    @Override public void initialize(URL url, ResourceBundle resourceBundle){
        if (helper.helperMethods.isInFrench()) {
            helper.helperMethods.setTextTo(loginLbl, "login");
            helper.helperMethods.setTextTo(userNameLbl, "username");
            helper.helperMethods.setTextTo(passwordLbl, "password");
            helper.helperMethods.setTextTo(locationLbl, "location");
            helper.helperMethods.setBtnTextTo(loginBtn, "login");
            helper.helperMethods.setBtnTextTo(exitBtn, "exit");
        }
        zoneIdLbl.setText(ZoneId.systemDefault().toString());
    }
}
