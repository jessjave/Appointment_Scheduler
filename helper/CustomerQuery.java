package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**This class defines methods for dealing with Customer objects. */
public class CustomerQuery {


    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**Inserts a new Customer object into the database.
     * @param address customers address
     * @param customerName name of the customer
     * @param divisionId ID of the state/province
     * @param phoneNumber phone number
     * @param postalCode postal code
     * @return rows affected by method. >0 means method was successful */
    public static int insert(String customerName, String address, String postalCode, String phoneNumber, int divisionId) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNumber);
        ps.setInt(5, divisionId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**Updates a new Customer object in the database.
     * @param address customers address
     * @param customerName name of the customer
     * @param divisionId ID of the state/province
     * @param phone phone number
     * @param postalCode postal code
     * @param customerId ID of the customer needing updated
     * @return rows affected by method. >0 means method was successful */
    public static int update(String customerName, String address, String postalCode, String phone, int divisionId, int customerId) throws SQLException {
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionId);
        ps.setInt(6, customerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**Deletes a customer object from the database.
     * @param customer selected Customer object
     * @return rows affected by method. >0 means method was successful */
    public static int delete(Customer customer) throws SQLException {
        String sql = "DELETE FROM APPOINTMENTS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customer.getCustomerId());
        int rowsAffected = ps.executeUpdate();
        String sql2 = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps2 = JDBC.connection.prepareStatement(sql2);
        ps2.setInt(1, customer.getCustomerId());
        int rowsAffected2 = ps2.executeUpdate();
        return rowsAffected;
    }

    /**This method gets all Customer objects from the database and places them in an observable list.
     * @return allCustomers a list of all customer objects and their attributes. */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        try {
            clearAllCustomers();
            String sql = "SELECT * FROM CUSTOMERS JOIN FIRST_LEVEL_DIVISIONS ON CUSTOMERS.Division_ID=FIRST_LEVEL_DIVISIONS.Division_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                Customer customer = new Customer(customerId, customerName, customerAddress, postalCode, phone, divisionId, divisionName);
                allCustomers.add(customer);
                }
            }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
            return allCustomers;
        }

        /**Clears all Customer objects from a list.
         * Mostly used for decreasing duplicate operations leading to repetitive entries displayed in TableViews or combo boxes. */
        public static ObservableList<Customer> clearAllCustomers() throws SQLException {
            allCustomers.clear();
            return allCustomers;
        }

        /**Checks if an input is empty if it is expecting a String variable.
         * @param variable the expected String variable that should not be empty
         * @param type the attribute that the String represents */
        public static void ifStringEmpty(String variable, String type) {
            if (variable.isEmpty()) {
                helperMethods.displayAlert("Please enter value for the " + type + " field.");
            }
        }
    }

