package Dal;
import Model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;

public class OrderData {
    // For reading order objects from a .dat file
    private void writeOrders(ObservableList<Order> orders) {
        try (FileOutputStream fos = new FileOutputStream("src/Resources/Files/orders.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            for (Order order : orders) {
                oos.writeObject(order);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //for refreshing the orders document
    public void writeOrder(Order order) {
        ArrayList<Order> orders = readOrders();
        orders.add(order);
        writeOrders(FXCollections.observableArrayList(orders));
    }

    // For reading orders
    public ArrayList<Order> readOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream("src/Resources/Files/orders.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    Order order = (Order) ois.readObject();
                    orders.add(order);
                } catch (EOFException eofe) { // Inspired from the lecture
                    break;
                }

            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orders;
    }

    //For creating order.
    public int getTheLatestID(){
        ArrayList<Order> orders= readOrders();
        int biggest=0;
        if(orders.stream().count()==0){
            return 1000;
        }
        for (Order o:orders){
            if(o.getOrderID()>biggest){
                biggest=o.getOrderID();
            }
        }
        return biggest+1;
    }
}
