package model;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**Class for First Level Divisions.
 * Referred to as State/Province in other parts of the program/document. */
public class FirstLevelDivision {

    private int divisionId;
    private String divisionName;
    private int countryId;

    /**Constructor for First Level Division objects.
     * @param divisionName name of the first level division
     * @param countryId ID of country associated with first level division
     * @param divisionId ID of the first level division*/
    public FirstLevelDivision(int divisionId, String divisionName, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    /**Returns a list of all first level division objects. */
    public static ObservableList<FirstLevelDivision> getAllFLDs() {
        ObservableList<FirstLevelDivision> listFLD = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * from first_level_divisions";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs.getInt("Country_ID");
                FirstLevelDivision firstLevelDivision = new FirstLevelDivision(divisionId, divisionName, countryId);
                listFLD.add(firstLevelDivision);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return listFLD;
    }
    /**Displays division name as a String for TableView. */
    @Override public String toString(){
        return (divisionName);
    }

    /**Getters and Setters for First Level Division objects. */
    public int getDivisionId() {return divisionId;}
    public void setDivisionId(int divisionId) {this.divisionId = divisionId;}

    public String getDivisionName() {return divisionName;}
    public void setDivisionName(String divisionName) {this.divisionName = divisionName;}

    public int getCountryId() {return countryId;}
    public void setCountryId(int countryId) {this.countryId = countryId;}

}
