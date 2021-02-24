package Scene;

import Dal.GuitarData;
import Dal.OrderData;
import Dialogs.ConfirmOrderDialog;
import Dialogs.CustomerDialog;
import Dialogs.GuitarDialog;
import Model.Customer;
import Model.Guitar;
import Model.Order;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;

public class SalesScene extends Application {

    //Sales scene is for adding an order to the system and it is only for sales employees.
    //These instances should be reachable from all methods.

    BorderPane pane;
    Customer selectedCustomer;
    Label welcomeLabel;

    private final ObservableList<ObservableMap.Entry<Guitar, Integer>> selectedGuitars = FXCollections.observableArrayList();
    final TableView<ObservableMap.Entry<Guitar, Integer>> orderView = new TableView<>();


    public SalesScene(BorderPane pane) {
        this.pane = pane;
    }

    public static void main(String[] args) {
        launch(args);
    }

    //I have a big start method sorry for that but I tried to make it as clean as possible.
    @Override
    public void start(Stage stage) {
        stage.setTitle("Guitar FX- Add orders");
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(40, 0, 0, 20));
        gridPane.setVgap(10);
        welcomeLabel = new Label(String.format("Create Order #%s",new OrderData().getTheLatestID()));
        GridPane.setConstraints(welcomeLabel, 0, 0);

        Label customerLbl = new Label("Customer");
        GridPane.setConstraints(customerLbl, 0, 2);

        TextField searchField = new TextField();
        GridPane.setConstraints(searchField, 0, 3);
        Button searchBtn = new Button("Search");
        GridPane.setConstraints(searchBtn, 1, 3);
        gridPane.getChildren().addAll(welcomeLabel, customerLbl, searchField, searchBtn);

        GridPane infoPane = new GridPane();
        prepareInfoPane(infoPane);
        ColumnConstraints constraints = new ColumnConstraints(300);
        infoPane.getColumnConstraints().addAll(constraints, constraints);
        prepareTableView();
        //For showing a customer list
        searchBtn.setOnAction(event -> {
            searchButtonAction(stage,searchField,infoPane);
        });
        HBox buttons = new HBox();
        Button add = new Button("Add");
        Button delete = new Button("Delete");
        Button confirm = new Button("Confirm");
        Button reset = new Button("Reset");
        buttons.getChildren().addAll(add, delete, confirm, reset);
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);
        //for adding a guitar
        add.setOnAction(event -> {
            addButtonAction(stage);
        });
        delete.setOnAction(event -> {
            deleteButtonAction();
        });
        confirm.setOnAction(event -> {
            //Confirming the order and writing it to a file via confirm order dialog
            confirmButtonAction(stage, infoPane);
        });
        reset.setOnAction(event -> {
            //since we reset the stocks guitars need to be updated back to their own state.
            new GuitarData().updateStockData(selectedGuitars, false);
            searchField.clear();
            resetScene(infoPane);
        });
        VBox bottomContainer = new VBox();
        bottomContainer.setPadding(new Insets(10, 10, 10, 0));
        bottomContainer.setSpacing(10);
        bottomContainer.getChildren().addAll(orderView, buttons);

        pane.setLeft(gridPane);
        pane.setRight(infoPane);
        pane.setBottom(bottomContainer);
    }

    //For add button
    private void addButtonAction(Stage stage) {
        GuitarDialog dialog = new GuitarDialog(stage);
        dialog.getStage().showAndWait();
        if (dialog.getQuantity() != null && dialog.getSelectedGuitar() != null) {
            insertGuitarsToMap(dialog.getQuantity(), dialog.getSelectedGuitar());
            //Updating the stock of the guitars added to the list locally
            ObservableList<ObservableMap.Entry<Guitar, Integer>> addedGuitars = FXCollections.observableArrayList();
            addedGuitars.add(Map.entry(dialog.getSelectedGuitar(), dialog.getQuantity()));
            new GuitarData().updateStockData(addedGuitars, true);
            updateTableview();
        }
    }
    //For search button
    private void searchButtonAction(Stage stage,TextField searchField,GridPane infoPane){
        CustomerDialog dialog = new CustomerDialog(stage, searchField.getText().toLowerCase());//lowercase for comparison
        dialog.getStage().showAndWait();
        selectedCustomer = dialog.getSelectedCustomer();
        if (selectedCustomer != null)
            updateCustomerInformation(infoPane);
    }
//For confirm button
    private void confirmButtonAction(Stage stage, GridPane infoPane) {
        if (selectedGuitars.stream().count()>0 && selectedCustomer != null) {
            Order newOrder = new Order(selectedCustomer, returnGuitars());
            ConfirmOrderDialog dialog = new ConfirmOrderDialog(stage, newOrder);
            dialog.getStage().showAndWait();
            if (!dialog.isConfirmed()) {
                //If order is canceled, add the guitars back to the stock
                new GuitarData().updateStockData(selectedGuitars, false);
            }
            resetScene(infoPane);
        }
    }
//For delete button
    private void deleteButtonAction() {
        Map.Entry<Guitar, Integer> entry = orderView.getSelectionModel().getSelectedItem();
        ObservableList<ObservableMap.Entry<Guitar, Integer>> deletedGuitars = FXCollections.observableArrayList();
        //Get the selected row
        Map.Entry<Guitar, Integer> newEntry = orderView.getSelectionModel().getSelectedItem();
        deletedGuitars.add(newEntry);
        //update the row
        if (orderView.getSelectionModel().getSelectedItem() != null) {
            new GuitarData().updateStockData(deletedGuitars, false);
            // clean the table views and update the local Observable list
            orderView.getItems().remove(entry);
            selectedGuitars.remove(entry);
        }
    }


    //Conversion method for Hashmaps
    private HashMap<Guitar, Integer> returnGuitars() {
        HashMap<Guitar, Integer> guitars = new HashMap<>();
        for (Map.Entry<Guitar, Integer> set : selectedGuitars) {
            guitars.put(set.getKey(), set.getValue());
        }
        return guitars;
    }

    //Resetting the scene
    private void resetScene(GridPane infoPane) {
        prepareInfoPane(infoPane);
        welcomeLabel.setText(String.format("Create Order #%s",new OrderData().getTheLatestID()));
        selectedCustomer = null;
        selectedGuitars.clear();
        ObservableList<Map.Entry<Guitar, Integer>> list = orderView.getSelectionModel().getSelectedItems();
        orderView.getItems().clear();
        selectedGuitars.removeAll(list);
    }

    //Inserts guitar to the map.
    private void insertGuitarsToMap(Integer quantity, Guitar guitar) {

        if (isGuitarPresent(guitar)) {
            Integer newAmount = selectedGuitars.get(selectedGuitars.indexOf(returnEntry(guitar))).getValue() + quantity;
            selectedGuitars.set(selectedGuitars.indexOf(returnEntry(guitar)), Map.entry(guitar, newAmount));
        } else {
            selectedGuitars.add(Map.entry(guitar, quantity));
        }
    }

    //Getting the right entry for the chosen guitar
    private Map.Entry<Guitar, Integer> returnEntry(Guitar guitar) {
        for (Map.Entry<Guitar, Integer> entry : selectedGuitars) {
            if (entry.getKey().getModel().equals(guitar.getModel())) {
                return entry;
            }
        }
        return null;
    }

    //Checking if guitar is present.
    private boolean isGuitarPresent(Guitar guitar) {
        for (Map.Entry<Guitar, Integer> entry : selectedGuitars) {
            if (entry.getKey().getModel().equals(guitar.getModel())) {
                return true;
            }
        }
        return false;
    }

    //refreshing the guitar table view
    private void updateTableview() {
        orderView.getItems().clear();
        ObservableList<Map.Entry<Guitar, Integer>> list = FXCollections.observableArrayList(selectedGuitars);
        orderView.setItems(list);
        // Add All the guitars and the quantities here.
    }

    // preparing the table view for guitars.
    private void prepareTableView() {

        TableColumn<ObservableMap.Entry<Guitar, Integer>, String> quantity = new TableColumn<>("Quantity");
        quantity.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String> cell) {
                return new SimpleStringProperty(cell.getValue().getValue().toString());
            }
        });
        quantity.setMinWidth(150);


        TableColumn<ObservableMap.Entry<Guitar, Integer>, String> brand = new TableColumn<>("Brand");
        brand.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String> cell) {
                return new SimpleStringProperty(cell.getValue().getKey().getBrand());
            }
        });
        brand.setMinWidth(150);

        TableColumn<ObservableMap.Entry<Guitar, Integer>, String> model = new TableColumn<>("Model");
        model.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String> cell) {
                return new SimpleStringProperty(cell.getValue().getKey().getModel());
            }
        });
        model.setMinWidth(150);

        TableColumn<ObservableMap.Entry<Guitar, Integer>, String> type = new TableColumn<>("Type");
        type.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String> cell) {
                return new SimpleStringProperty(cell.getValue().getKey().getType().toString());
            }
        });


        TableColumn<ObservableMap.Entry<Guitar, Integer>, String> price = new TableColumn<>("Price");
        price.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String> cell) {
                return new SimpleStringProperty(String.valueOf(cell.getValue().getKey().getPrice()));
            }
        });

        orderView.getColumns().addAll(quantity, brand, model, type, price);
        ScrollPane scPane = returnScrollPane();
        scPane.setContent(orderView);
    }

    private ScrollPane returnScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scrollPane;

    }

    // preparing the grid pane for customer information
    private void prepareInfoPane(GridPane infoPane) {
        infoPane.getChildren().clear();
        infoPane.setVgap(10);
        infoPane.setHgap(10);
        infoPane.setId("infoPane");

        infoPane.setPadding(new Insets(60, 20, 0, 40));

        Label firstName = new Label("First Name:");
        GridPane.setConstraints(firstName, 0, 0);
        Label lastName = new Label("Last Name:");
        GridPane.setConstraints(lastName, 1, 0);
        Label phoneNumber = new Label("Phone Number:");
        GridPane.setConstraints(phoneNumber, 1, 1);
        Label streetAddress = new Label("Street Address:");
        GridPane.setConstraints(streetAddress, 0, 1);
        Label city = new Label("City:");
        GridPane.setConstraints(city, 0, 2);
        Label emailAddress = new Label("Email Address:");
        GridPane.setConstraints(emailAddress, 1, 2);
        infoPane.getChildren().addAll(firstName, lastName, phoneNumber, streetAddress, city, emailAddress);
        resizeInfoLabels(infoPane);
    }


    // changing the labels within the customer grid pane
    private void updateCustomerInformation(GridPane infoPane) {
        infoPane.getChildren().clear();
        Label firstName = new Label(String.format("First Name: %s", selectedCustomer.getFirstName()));
        GridPane.setConstraints(firstName, 0, 0);
        Label lastName = new Label(String.format("Last Name: %s", selectedCustomer.getLastName()));
        GridPane.setConstraints(lastName, 1, 0);
        Label city = new Label(String.format("City: %s", selectedCustomer.getCity()));
        GridPane.setConstraints(city, 0, 2);
        Label phoneNumber = new Label(String.format("Phone Number: %s", selectedCustomer.getPhoneNumber()));
        GridPane.setConstraints(phoneNumber, 1, 1);
        Label streetAddress = new Label(String.format("Street Address: %s", selectedCustomer.getStreetAddress()));
        GridPane.setConstraints(streetAddress, 0, 1);
        Label emailAddress = new Label(String.format("Email Address: %s", selectedCustomer.getEmail()));
        GridPane.setConstraints(emailAddress, 1, 2);

        infoPane.getChildren().addAll(firstName, lastName, phoneNumber, streetAddress, city, emailAddress);
        resizeInfoLabels(infoPane);
    }

    //just an adjustment method for css
    private void resizeInfoLabels(GridPane infoPane) {
        for (Node n : infoPane.getChildren()) {
            n.setId("infoLabel");
        }
    }
}
