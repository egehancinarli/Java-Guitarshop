package Model;

import java.io.Serializable;

public class Manager extends Employee implements Serializable {


    public Manager(String firstName, String lastName, String email, String password, String username) {
        super(firstName, lastName, email, password, username);
        accessLevel=AccessLevel.Manager;
    }
}
