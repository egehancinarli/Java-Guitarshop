package Dialogs;

import Dal.CustomerData;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.inholland.exam.Question3.AddCustomerDialog;

import java.util.ArrayList;

public class CustomerDialog {

    //Global variables
    private Stage stage;
    private String customerName;
    private Customer selectedCustomer;

    public CustomerDialog(Stage ownerStage, String customerName) {
        this.stage = new Stage();
        this.customerName = customerName;
        stage.initOwner(ownerStage);
        stage.initModality(Modality.WINDOW_MODAL);
        prepareStage();

    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public Stage getStage() {
        return stage;
    }

    private void prepareStage() {
        BorderPane root = new BorderPane();
        stage.setResizable(false);
        Label welcomeLabel = new Label("Welcome!! Please pick a customer");
        welcomeLabel.setId("welcomeLabel");
        root.setLeft(welcomeLabel);
        stage.setWidth(850);
        stage.setHeight(600);

        TableView<Customer> customerTableView = new TableView();
        prepareTableView(customerTableView);

        VBox bottomContainer = new VBox();
        bottomContainer.setSpacing(15);


        CustomerData customerData = new CustomerData();
        ArrayList<Customer> customerList = customerData.readCustomers();

        ObservableList<Customer> customerObList = FXCollections.observableArrayList(customerList);
        fillTheTableView(customerTableView, customerObList);


        //For setting the selected customer.
        customerTableView.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && (!row.isEmpty())) {
                    selectedCustomer = (Customer) row.getItem();
                    stage.close();
                }
            });
            return row;
        });

        Button addCustomer = new Button("Add Customer:");
        bottomContainer.getChildren().addAll(customerTableView, addCustomer);
        bottomContainer.setPadding(new Insets(0,0,20,0));
        root.setBottom(bottomContainer);
        addCustomer.setOnAction(e->{
            AddCustomerDialog dialog= new AddCustomerDialog(stage);
            dialog.getStage().showAndWait();
            ArrayList<Customer> newCustomerList = customerData.readCustomers();
            //for updating the customer list
            fillTheTableView(customerTableView,FXCollections.observableArrayList(newCustomerList));

        });


        Scene scene = new Scene(root);
        scene.getStylesheets().add("Resources/Styles/style.css");
        stage.setScene(scene);
    }


    //For preparing table view
    void prepareTableView(TableView<Customer> customerTableView) {

        TableColumn<Model.Customer, String> firstName = new TableColumn<>("First Name");
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstName.setMinWidth(150);

        TableColumn<Model.Customer, String> lastName = new TableColumn<>("Last Name");
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastName.setMinWidth(150);

        TableColumn<Model.Customer, String> streetAddress = new TableColumn<>("Address");
        streetAddress.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        streetAddress.setMinWidth(150);

        TableColumn<Model.Customer, String> city = new TableColumn<>("City");
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        city.setMinWidth(100);


        TableColumn<Model.Customer, Long> phoneNumber = new TableColumn<>("Phone#");
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneNumber.setMinWidth(150);

        TableColumn<Model.Customer, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        email.setMinWidth(150);

        customerTableView.getColumns().addAll(firstName, lastName, email, phoneNumber, city, streetAddress);
    }

    //For entering items to table view

    void fillTheTableView(TableView orderTable, ObservableList<Customer> customerList) {
        orderTable.getItems().clear();
        for (Customer c : customerList) {
            if (c.getFullName().toLowerCase().contains(customerName)) {
                orderTable.getItems().add(c);
            }
        }
    }
}


