package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**Class for Country objects and associated methods. */
public class Countries {
    private int countryId;
    private String countryName;

    /**Constructor for Country objects.
     * @param countryId ID for country
     * @param countryName name of country*/
    public Countries(int countryId, String countryName){
        this.countryId = countryId;
        this.countryName = countryName;
    }

    /**Gets a list of all Country objects. */
    public static ObservableList<Countries> getAllCountries() {
        ObservableList<Countries> countryList = FXCollections.observableArrayList();
        try {
            countryList.clear();
            String sql = "SELECT * from countries";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Countries country = new Countries(countryId, countryName);
                countryList.add(country);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return countryList;
    }

    /**Converts country ID to a string to display the name in the combo box. */
    @Override public String toString(){
        return (countryName);
    }

    /**Getters and Setters for Country object attributes. */
    public int getCountryId() {return countryId;}
    public void setCountryId(int countryId) {this.countryId = countryId;}

    public String getCountryName() {return countryName;}
    public void setCountryName(String countryName) {this.countryName = countryName;}
}
