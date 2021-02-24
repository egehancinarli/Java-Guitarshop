package Dal;

import Model.Employee;
import Model.Manager;
import Model.SalesPerson;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeData {
    // This class is mostly about initializing, reading and writing data for regarding employees.
    // For reading employee objects from a .dat file

    //For creating the employees.dat file with few employee examples.
    public void writeEmployees(ObservableList<Employee> employees) {
        try (FileOutputStream fos = new FileOutputStream("src/Resources/Files/employees.dat",true);
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            for (Employee employee : employees) {
                    oos.writeObject(employee);
            }
        } catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Verifies the credentials to see the user
    public Employee verifyCredentials(String username, String password){
       List<Employee> employees = readEmployees();
       Employee employee=null; // If the employee is null, the logging process is failed.
       for(Employee e :employees){
           if(e.getUsername().toLowerCase().equals(username.toLowerCase())&&e.getPassword().equals(password)){
               employee=e;
           }
       }
       return employee;
    }

    // Reading the file for login page.
    public List<Employee> readEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream("src/Resources/Files/employees.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while(true){
                try{
                    Employee employee;
                    if(ois.readObject() instanceof Manager){
                         employee = (Manager) ois.readObject();
                    }
                    else{
                         employee = (SalesPerson) ois.readObject();
                    }
                    employees.add(employee);
                }catch(EOFException eofe){ // Inspired from the lecture
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

        return employees;
    }

}
