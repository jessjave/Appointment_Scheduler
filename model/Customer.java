package model;

/**Class for Customer objects and associated methods. */
public class Customer {

    private String divisionName;
    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhone;
    private int divisionId;

    /**Customer object constructor.
     * @param customerId ID of customer
     * @param divisionId ID of state/province
     * @param customerName name of customer
     * @param customerAddress address of customer
     * @param customerPhone phone number of customer
     * @param customerPostalCode postal code of customer
     * @param divisionName name of the state/province*/
    public Customer(int customerId, String customerName, String customerAddress, String customerPostalCode, String customerPhone, int divisionId, String divisionName){
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        this.divisionId = divisionId;
        this.divisionName = divisionName;
    }
    /**Displays name as a string for TableView display. */
    @Override public String toString(){
        return (customerName);
    }

    /**Getters and Setters for Customer object attributes. */
    public int getCustomerId() {return customerId;}
    public void setCustomerId(int customerId) {this.customerId = customerId;}

    public String getCustomerName() {return customerName;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}

    public String getCustomerAddress() {return customerAddress;}
    public void setCustomerAddress(String customerAddress) {this.customerAddress = customerAddress;}

    public String getCustomerPostalCode() {return customerPostalCode;}
    public void setCustomerPostalCode(String customerPostalCode) {this.customerPostalCode = customerPostalCode;}

    public String getCustomerPhone() {return customerPhone;}
    public void setCustomerPhone(String customerPhone) {this.customerPhone = customerPhone;}

    public int getDivisionId() {return divisionId;}
    public void setDivisionId(int divisionId) {this.divisionId = divisionId;}

    public String getDivisionName() {return divisionName;}

    public void setDivisionName(String divisionName) {this.divisionName = divisionName;}
}
