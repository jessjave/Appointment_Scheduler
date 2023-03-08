package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**Class that deals with Appointment objects and associated methods. */
public class Appointments {
    private int apptId;
    private String title;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String description;
    private String startD;
    private String endD;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;
    private int contactId;
    private String contactName;
    private static ObservableList<Appointments> allAppts = FXCollections.observableArrayList();

    /**Constructor for Appointment objects utilizing LocalDateTimes for start and end.
     * @param userId ID for selected user
     * @param type type of appointment
     * @param customerId ID associated with designated customer
     * @param apptId ID of appointment object
     * @param contactId ID of associated contact
     * @param contactName name of contact
     * @param description description of appointment
     * @param end time of end of appointment stored as LocalDateTime
     * @param location location of appointment
     * @param title title of appointment
     * @param start time of start of appointment stored as LocalDateTime*/
    public Appointments(int apptId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId, String contactName) {
        this.apptId = apptId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**Constructor for Appointment objects utilizing LocalDateTimes for start and end.
     * @param userId ID for selected user
     * @param type type of appointment
     * @param customerId ID associated with designated customer
     * @param apptId ID of appointment object
     * @param contactId ID of associated contact
     * @param contactName name of contact
     * @param description description of appointment
     * @param endD time of end of appointment stored as String
     * @param location location of appointment
     * @param title title of appointment
     * @param startD time of start of appointment stored as String*/
    public Appointments(int apptId, String title, String description, String location, String type, String startD, String endD, int customerId, int userId, int contactId, String contactName) {
        this.apptId = apptId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startD = startD;
        this.endD = endD;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**Gets a list of all Appointment objects with LocalDateTime start and end variables. */
    public static ObservableList<Appointments> getAllAppts() throws SQLException {
        try {
            allAppts.clear();
            String sql = "SELECT * FROM APPOINTMENTS JOIN CONTACTS ON APPOINTMENTS.Contact_ID=CONTACTS.Contact_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = LocalDateTime.parse(rs.getString("Start"), formatter);
                LocalDateTime end = LocalDateTime.parse(rs.getString("End"), formatter);
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                Appointments appointment = new Appointments(apptId, title, description, location, type, start, end, customerId, userId, contactId, contactName);
                allAppts.add(appointment);

            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allAppts;
    }

    /**Gets a list of all Appointment objects with String start and end variables. */
    public static ObservableList<Appointments> getAllApptsForTable() throws SQLException {
        try {
            allAppts.clear();
            String sql = "SELECT * FROM APPOINTMENTS JOIN CONTACTS ON APPOINTMENTS.Contact_ID=CONTACTS.Contact_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {


                int apptId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");

                ZoneId userZone = ZoneId.systemDefault();

                ZonedDateTime zoneStart = rs.getTimestamp("Start").toInstant().atZone(userZone);
                LocalDateTime startDT = zoneStart.toLocalDateTime();
                String start = formatter.format(startDT);
                LocalDateTime endD = rs.getTimestamp("End").toInstant().atZone(userZone).toLocalDateTime();
                //String end = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(endD);
                String end = formatter.format(endD);


                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                Appointments appointment = new Appointments(apptId, title, description, location, type, start, end, customerId, userId, contactId, contactName);
                allAppts.add(appointment);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allAppts;
    }

    /**Deletes Appointment objects from the database.
     * @param appointment selected Appointment object. */
    public static int delete(Appointments appointment) throws SQLException {
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointment.getApptId());
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**Checks appointment times with submitted time to ensure no overlapping appointments.
     * @param start starting time of appointment attempting to be input
     * @param end ending time of appointment attempting to be input*/
    public static boolean checkOverlap(Timestamp start, Timestamp end) throws SQLException {
        String sql = "SELECT * FROM client_schedule.appointments\n" +
                "WHERE ? > Start AND ? < End\n" +
                "OR ? > Start AND ? < End\n" +
                "OR Start = ? \n" +
                "OR End = ? \n" +
                "OR Start > ? AND Start < ? \n" +
                "OR End > ? AND End < ? LIMIT 1 \n";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setTimestamp(1, start);
        ps.setTimestamp(2, end);
        ps.setTimestamp(3, end);
        ps.setTimestamp(4, start);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setTimestamp(7, start);
        ps.setTimestamp(8, end);
        ps.setTimestamp(9, start);
        ps.setTimestamp(10, end);

        ResultSet rs = ps.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        if (count > 0){
            return false;
        }
        else{
            return true;
        }
    }

    /**Checking to ensure appointment being input is within business hours.
     * @param start start time of appointment being input
     * @param end end time of appointment being input. */
    public static boolean checkBusinessHours(Timestamp start, Timestamp end) {
        ZoneId est = ZoneId.of("America/New_York");
        ZonedDateTime startZdt = start.toLocalDateTime().atZone(est);
        ZonedDateTime endZdt = end.toLocalDateTime().atZone(est);
        LocalTime startLocalTime = startZdt.toLocalTime();
        LocalTime endLocalTime = endZdt.toLocalTime();
        LocalDate startLocalDate = startZdt.toLocalDate();
        LocalDate endLocalDate = endZdt.toLocalDate();

        DayOfWeek dayOfWeekStart = startLocalDate.getDayOfWeek();
        DayOfWeek dayOfWeekEnd = endLocalDate.getDayOfWeek();

        if(dayOfWeekStart.getValue() > 5 || dayOfWeekEnd.getValue() > 5){
            return false;
        }
        if(startLocalTime.isBefore(LocalTime.of(8,0)) || startLocalTime.isAfter(LocalTime.of(22,0))){
            return false;
        }
        if(endLocalTime.isBefore(LocalTime.of(8,0)) || endLocalTime.isAfter(LocalTime.of(22,0))){
            return false;
        }
        return true;
    }

    /**Inserts new Appointment object into the database.
     * Input data validation for overlapping times and ensuring within business hours is completed.
     * @param userId ID for selected user
     * @param type type of appointment
     * @param customerId ID associated with designated customer
     * @param contactId ID of associated contact
     * @param description description of appointment
     * @param end time of end of appointment stored as TimeStamp
     * @param location location of appointment
     * @param title title of appointment
     * @param start time of start of appointment stored as TimeStamp*/
    public static void insert(String title, String description, String location, String type, int customerId, int userId, int contactId, Timestamp start, Timestamp end) throws SQLException {

        if(!checkOverlap(start, end)){
            helper.helperMethods.displayAlert("This appointment time overlaps with another. Please choose a different time or date.");
        }

        else if (!checkBusinessHours(start, end)){
            helper.helperMethods.displayAlert("Appointment is outside of business hours (Mon-Fri 8am to 10pm EST)");
        }
        else {
            String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Customer_ID, User_ID, Contact_ID, Start, End) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setInt(5, customerId);
            ps.setInt(6, userId);
            ps.setInt(7, contactId);
            ps.setTimestamp(8, start);
            ps.setTimestamp(9, end);
            int rowsAffected = ps.executeUpdate();
        }
    }

    /**Updates current Appointment object in the database.
     * Input data validation for overlapping times and ensuring within business hours is completed.
     * @param userId ID for selected user
     * @param type type of appointment
     * @param customerId ID associated with designated customer
     * @param contactId ID of associated contact
     * @param description description of appointment
     * @param end time of end of appointment stored as TimeStamp
     * @param location location of appointment
     * @param title title of appointment
     * @param apptId ID of selected appointment
     * @param start time of start of appointment stored as TimeStamp*/
    public static void update(String title, String description, String location, String type, int customerId, int userId, int contactId, int apptId, Timestamp start, Timestamp end) throws SQLException {

        if (!checkOverlap(start, end)) {
            helper.helperMethods.displayAlert("This appointment time overlaps with another. Please choose a different time or date.");
        }

        else if (!checkBusinessHours(start, end)){
            helper.helperMethods.displayAlert("Appointment is outside of business hours (Mon-Fri 8am to 10pm EST)");
        }
        else {
            String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?, Start = ?, End = ? WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setInt(5, customerId);
            ps.setInt(6, userId);
            ps.setInt(7, contactId);
            ps.setTimestamp(8, start);
            ps.setTimestamp(9, end);
            ps.setInt(10, apptId);
            int rowsAffected = ps.executeUpdate();
        }

    }

    /**Getters and Setters for all Appointment object attributes. */
    public int getApptId() {return apptId;}
    public void setApptId(int apptId) {this.apptId = apptId;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getLocation() {return location;}
    public void setLocation(String location) {this.location = location;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public LocalDateTime getStart() {return start;}
    public void setStart(LocalDateTime start) {this.start = start;}

    public LocalDateTime getEnd() {return end;}
    public void setEnd(LocalDateTime end) {this.end = end;}

    public int getCustomerId() {return customerId;}
    public void setCustomerId(int customerId) {this.customerId = customerId;}

    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}

    public int getContactId() {return contactId;}
    public void setContactId(int contactId) {this.contactId = contactId;}

    public String getContactName() {return contactName;}

    public void setContactName(String contactName) {this.contactName = contactName;}

    public String getStartD() {return startD;}

    public void setStartD(String startD) {this.startD = startD;}

    public String getEndD() {return endD;}

    public void setEndD(String endD) {this.endD = endD;}



}
