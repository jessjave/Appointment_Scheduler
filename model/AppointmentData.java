package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**Creates AppointmentData objects to populate the TableView in the Appointment type and month report. */
public class AppointmentData {

        private String type;
        private String month;
        private int count;
        private static ObservableList<AppointmentData> apptData = FXCollections.observableArrayList();

        /**Constructor for the AppointmentData objects.
         * @param type the type of appointment
         * @param month the month of the appointment
         * @param count how many of each type of appointment belong to the corresponding month*/
        public AppointmentData(String type, String month, int count) {
            this.type = type;
            this.month = month;
            this.count = count;
        }

    /**Creates a list of all AppointmentData objects for TableView population.*/
    public static ObservableList<AppointmentData> getAllApptData() throws SQLException {
        try {
            apptData.clear();
            String sql = "SELECT \n" +
                    "    Type, \n" +
                    "    MONTHNAME(Start) as month, \n" +
                    "    COUNT(*) as count\n" +
                    "FROM \n" +
                    "    APPOINTMENTS\n" +
                    "GROUP BY \n" +
                    "    Type, \n" +
                    "    MONTH(Start)\n" +
                    "ORDER BY \n" +
                    "    MONTH(Start);";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("type");
                String month = rs.getString("month");
                int count = rs.getInt("count");

                AppointmentData appointmentData = new AppointmentData(type, month, count);
                apptData.add(appointmentData);

            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return apptData;
    }

        /**Getters for the type, month and count of appointments. */
        public String getType() {
            return type;
        }

        public String getMonth() {
            return month;
        }

        public int getCount() {
            return count;
        }
    }

