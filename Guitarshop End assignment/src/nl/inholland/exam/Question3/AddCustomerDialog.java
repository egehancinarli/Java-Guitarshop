package nl.inholland.exam.Question3;

import Dal.CustomerData;
import Dialogs.WarningDialog;
import Model.Customer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.TimeoutException;

public class AddCustomerDialog {
    // IMPORTANT NOTE: PLEASE ALSO CHECK CUSTOMER DIALOG, CUSTOMERDATA AND SALESSCENE CLASSES FOR EXTRA METHODS

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public AddCustomerDialog(Stage ownerStage) {
        this.stage = new Stage();
        stage.initOwner(ownerStage);
        stage.initModality(Modality.WINDOW_MODAL);
        prepareStage();

    }

    void prepareStage() {
        stage.setTitle("Guitar FX- Add a customer");
        Label welcomeLabel = new Label("Welcome!! Please fill the fields");
        welcomeLabel.setId("welcomeLabel");
        stage.setWidth(600);
        stage.setHeight(600);

        GridPane pane = new GridPane();
        pane.setVgap(20);

        Button confirmButton = new Button("Confim");

        Label firstName = new Label("First Name:");
        firstName.setId("contentLabel");
        TextField firstnameField = new TextField();
        pane.add(firstName, 0, 0);
        pane.add(firstnameField, 1, 0);

        Label lastName = new Label("Last Name:");
        lastName.setId("contentLabel");
        TextField lastnameField = new TextField();
        pane.add(lastName, 0, 1);
        pane.add(lastnameField, 1, 1);

        Label streetAddress = new Label("Street Address:");
        streetAddress.setId("contentLabel");
        pane.add(streetAddress, 0, 2);

        TextField streetAddressField = new TextField();
        pane.add(streetAddressField, 1, 2);

        Label city = new Label("City:");
        city.setId("contentLabel");
        TextField cityField = new TextField();
        pane.add(city, 0, 3);
        pane.add(cityField, 1, 3);

        Label phoneNumber = new Label("Phone number:");
        phoneNumber.setId("contentLabel");
        TextField phoneNumberField = new TextField("im here");

        pane.add(phoneNumber, 0, 4);
        pane.add(phoneNumberField, 1, 4);

        Label emailAddress = new Label("Email Address:");
        emailAddress.setId("contentLabel");
        TextField emailField = new TextField();
        pane.add(emailAddress, 0, 5);
        pane.add(emailField, 1, 5);
        pane.add(confirmButton, 3, 6);

        Label warningLabel = new Label();
        warningLabel.setId("warningLabel");
        pane.add(warningLabel, 0, 7);

        confirmButton.setOnAction(e -> {

            if (emailField.getText().isEmpty() || cityField.getText().isEmpty() || firstnameField.getText().isEmpty() || lastnameField.getText().isEmpty() || streetAddressField.getText().isEmpty()||phoneNumberField.getText().isEmpty()) {
                new WarningDialog(stage, "Please fill all the fields", "Fields required to be filled").getStage().showAndWait();
            } else {

                Customer newCustomer = new Customer(firstnameField.getText(), lastnameField.getText(), emailAddress.getText(), phoneNumberField.getText(), cityField.getText(), streetAddressField.getText());
                new CustomerData().addOneCustomer(newCustomer);
                stage.close();
            }
        });
        Scene scene = new Scene(pane);

        scene.getStylesheets().add("Resources/Styles/style.css");

        stage.setScene(scene);


    }
}
