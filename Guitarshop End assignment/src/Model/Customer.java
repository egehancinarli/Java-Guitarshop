package Model;

import java.io.Serializable;

public class Customer implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String city;
    private String streetAddress;

    public Customer(String firstName, String lastName, String email, String phoneNumber, String city, String streetAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.streetAddress = streetAddress;
    }

    public String getFullName() {
        return String.format(this.firstName + " " + this.lastName);
    }

    // Getters and setters for the variables.
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public final String getFirstName() {
        return firstName;
    }

    public final String getLastName() {
        return lastName;
    }

    public final String getEmail() {
        return email;
    }

    public final String getPhoneNumber() {
        return phoneNumber;
    }

    public final String getCity() {
        return city;
    }

    public final String getStreetAddress() {
        return streetAddress;
    }
}
