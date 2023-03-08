import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

/**This class creates an app for scheduling appointments. */
public class Main extends Application{

    /**This method designates the first screen of the GUI in preparation for launch.
     * It will load the login screen. */
        @Override
        public void start(Stage stage) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
            stage.setTitle("First Screen");
            stage.setScene(new Scene(root));
            stage.show();
        }

    /**This method launches the application and connects it to our database. It also closes the database connection when the application is exited. */
        public static void main(String[] args) throws SQLException, IOException {

            //Locale.setDefault(new Locale("fr"));

            helper.JDBC.openConnection();

            launch();

            helper.JDBC.closeConnection();

        }
    }
