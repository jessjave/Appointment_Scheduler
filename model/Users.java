package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**Class for User objects and associated methods. */
public class Users {

    private int userId;
    private String userName;
    private String userPassword;
    private static ObservableList<Users> allUsers = FXCollections.observableArrayList();

    /**Constructor for User object.
     * @param userId ID of user
     * @param userName username of user
     * @param userPassword password of user*/
    public Users(int userId, String userName, String userPassword){
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**Returns a list of all User objects. */
    public static ObservableList<Users> getAllUsers() throws SQLException {
        try {
            allUsers.clear();
            String sql = "SELECT * FROM USERS";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                Users user = new Users(userId, userName, password);
                allUsers.add(user);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allUsers;
    }
    /**Displays username as a String for combo box display. */
    @Override public String toString(){
        return (userName);
    }

    /**Getters and Setters for User attributes. */
    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}

    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}

    public String getUserPassword() {return userPassword;}
    public void setUserPassword(String userPassword) {this.userPassword = userPassword;}
}
