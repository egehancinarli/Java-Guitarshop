package Model;

import Dal.OrderData;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Order implements Serializable {
    private Customer customer;
    private  HashMap<Guitar,Integer> selectedGuitars;
    private double totalCost;
    private int orderID;
    private LocalDate date;
    private int countOfGuitars;

    // Constructor

    public Order(Customer customer, HashMap<Guitar,Integer>selectedGuitars) {
        this.customer = customer;
        this.selectedGuitars = selectedGuitars;
        this.totalCost = calculateTotalCost();
        this.countOfGuitars=selectedGuitars.size(); //only the count of different guitars.
        this.orderID= new OrderData().getTheLatestID();
        this.date =LocalDate.now();
    }

    private double calculateTotalCost(){
        double total=0;
        //Value(Integer) is the quantity of the guitar in this case
        for(Map.Entry<Guitar,Integer>set : selectedGuitars.entrySet()){
            total += set.getKey().getPrice() * set.getValue();
        }
        return total;
    }

    //Getters and Setters

    public void setSelectedGuitars(HashMap<Guitar, Integer> selectedGuitars) {
        this.selectedGuitars = selectedGuitars;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCountOfGuitars() {
        return countOfGuitars;
    }

    public void setCountOfGuitars(int countOfGuitars) {
        this.countOfGuitars = countOfGuitars;
    }

    public Customer getCustomer() {
        return customer;
    }

    public HashMap<Guitar,Integer> getSelectedGuitars() { return selectedGuitars; }

    public double getTotalCost() {
        return totalCost;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
