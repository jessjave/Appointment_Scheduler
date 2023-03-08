package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**Class dealing with Contact objects and associated methods. */
public class Contacts {

    private int contactId;
    private String contactName;
    private String email;
    private int apptId;
    private String title;
    private String type;
    private String description;
    private String start;
    private String end;
    private int customerId;
    private static ObservableList<Contacts> allContacts = FXCollections.observableArrayList();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**Contact constructor.
     * @param contactId ID of contact
     * @param contactName name of contact
     * @param email email address of contact. */
    public Contacts(int contactId, String contactName, String email) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    /**Contact constructor that includes relevant appointment information for the reports section of the application.
     * @param contactId ID of contact
     * @param contactName name of contact
     * @param apptId ID of appointment
     * @param title title of appointment
     * @param description description of appointment
     * @param type type of appointment
     * @param customerId ID of customer associated with appointment
     * @param start start time input as a String
     * @param end end time input as a String*/
    public Contacts(int contactId, String contactName, int apptId, String title, String type, String description, String start, String end, int customerId) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.apptId = apptId;
        this.title = title;
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
        this.customerId = customerId;

    }

    /**Gets a list of all Contact objects. */
    public static ObservableList<Contacts> getAllContacts() throws SQLException {
        try {
            allContacts.clear();
            String sql = "SELECT * FROM CONTACTS";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String emailAddress = rs.getString("Email");
                Contacts contact = new Contacts(contactId, contactName, emailAddress);
                allContacts.add(contact);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allContacts;
    }

    /**Gets a list of Contact objects including relevant appointment information for a report. */
    public static ObservableList<Contacts> getAllContactsForReport() throws SQLException {
        try {
            allContacts.clear();
            String sql = "SELECT * FROM CONTACTS JOIN APPOINTMENTS ON CONTACTS.Contact_ID=APPOINTMENTS.Contact_ID\n" +
                    "ORDER BY \n" +
                    "    Contact_Name,\n" +
                    "    Start;";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ZoneId userZone = ZoneId.systemDefault();

                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                int apptId = rs.getInt("Appointment_ID");

                ZonedDateTime zoneStart = rs.getTimestamp("Start").toInstant().atZone(userZone);
                LocalDateTime startDT = zoneStart.toLocalDateTime();
                String start = formatter.format(startDT);
                LocalDateTime endD = rs.getTimestamp("End").toInstant().atZone(userZone).toLocalDateTime();
                String end = formatter.format(endD);

                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String type = rs.getString("Type");
                int customerId = rs.getInt("Customer_ID");
                Contacts contacts = new Contacts(contactId, contactName, apptId, title, type, description, start, end, customerId);
                allContacts.add(contacts);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allContacts;
    }
    /**Returns the contact name as a String for TableView population. */
    @Override public String toString(){
        return (contactName);
    }

    /**Getters and Setters for Contact object attribultes. */
    public int getContactId() {return contactId;}
    public void setContactId(int contactId) {this.contactId = contactId;}

    public String getContactName() {return contactName;}
    public void setContactName(String contactName) {this.contactName = contactName;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public int getApptId() {return apptId;}
    public void setApptId(int apptId) {this.apptId = apptId;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getStart() {return start;}
    public void setStart(String start) {this.start = start;}

    public String getEnd() {return end;}
    public void setEnd(String end) {this.end = end;}

    public int getCustomerId() {return customerId;}
    public void setCustomerId(int customerId) {this.customerId = customerId;}

}
