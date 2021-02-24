package Model;


import java.io.Serializable;

public class SalesPerson extends Employee implements Serializable {

    public SalesPerson(String firstName, String lastName, String email, String password, String username) {
        super(firstName, lastName, email, password, username);
        accessLevel= AccessLevel.Sales;
    }
}
