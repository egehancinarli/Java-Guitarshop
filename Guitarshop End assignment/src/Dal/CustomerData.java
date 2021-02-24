package Dal;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.ArrayList;

public class CustomerData {
    // For reading customer objects from a .dat file

    private void writeCustomers(ObservableList<Customer> customers) {
        try (FileOutputStream fos = new FileOutputStream("src/Resources/Files/customers.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            for (Customer customer : customers) {
                oos.writeObject(customer);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // for reading customers
    public ArrayList<Customer> readCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream("src/Resources/Files/customers.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    Customer customer = (Customer) ois.readObject();
                    customers.add(customer);
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
        return customers;
    }

    // WRITTEN FOR CUSTOMER QUESTION
    public void addOneCustomer(Customer customer){
        ArrayList<Customer> allCustomers = readCustomers();
        allCustomers.add(customer);

        writeCustomers(FXCollections.observableArrayList(allCustomers));
        //Adding one customer to the general list and overwriting it.
    }
}
