package Scene;

import Dal.OrderData;
import Model.Guitar;
import Model.Order;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.time.LocalDate;

import java.util.Map;

public class ListOrderScene extends Application {

//This scene is for listing order function for both manager and sales person employee classes.

    //Instances below should be reachable from all methods
    private final TableView<ObservableMap.Entry<Guitar, Integer>> guitarTableView;
    private TableView<Order> orderTableView;

    public static void main(String[] args) {
        launch(args);
    }

    BorderPane pane;
    OrderData orderData;
    ObservableList<Order> allOrders;

    public ListOrderScene(BorderPane pane) {
        this.pane = pane;
        orderData = new OrderData();
        guitarTableView = new TableView<>();
        orderTableView = new TableView<>();
    }

    @Override
    public void start(Stage stage) {

        allOrders = FXCollections.observableArrayList(orderData.readOrders());
        orderTableView.setItems(allOrders);
        stage.setTitle("Guitar FX- List of orders");
        VBox container = new VBox();
        container.setPadding(new Insets(20, 0, 20, 0)); //Only top and bottom.
        container.setSpacing(40);
        Label orderListLbl = new Label("Order List:");
        Label guitarListLbl = new Label("Guitar List:");

        container.getChildren().addAll(orderListLbl, orderTableView, guitarListLbl, guitarTableView);

        //prepare the table views
        prepareOrderTableView();
        prepareGuitarTableView();

        orderTableView.setRowFactory(ov -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() >= 1 && (!row.isEmpty())) {
                    fillGuitarTableView();
                }
            });
            return row;
        });
        pane.setCenter(container);
    }

    //It refreshes, fills the guitar table view
    void fillGuitarTableView() {
        guitarTableView.getItems().clear();
        Order order = orderTableView.getSelectionModel().getSelectedItem();
        ObservableList<ObservableMap.Entry<Guitar, Integer>> guitars = FXCollections.observableArrayList();
        for (Map.Entry<Guitar, Integer> entry : order.getSelectedGuitars().entrySet()) {
            guitars.add(entry);
        }
        guitarTableView.setItems(guitars);
    }

    //initializes the table view for orders.
    void prepareOrderTableView() {
        TableColumn<Order, Integer> orderID = new TableColumn<>("Order #");
        orderID.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderID"));
        orderID.setMinWidth(100);

        TableColumn<Order, Double> totalCost = new TableColumn<>("Total");
        totalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        totalCost.setMinWidth(100);

        TableColumn<Order, LocalDate> date = new TableColumn<>("Date Of Order");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        date.setMinWidth(100);

        TableColumn<Order, String> customerName = new TableColumn<>("Customer name");
        customerName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> order) {
                return new SimpleStringProperty(order.getValue().getCustomer().getFullName());
            }
        });
        customerName.setMinWidth(200);

        TableColumn<Order, String> city = new TableColumn<>("City");
        city.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> order) {
                return new SimpleStringProperty(order.getValue().getCustomer().getCity());
            }
        });
        city.setMinWidth(50);

        TableColumn<Order, String> phone = new TableColumn<>("Phone #");
        phone.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> order) {
                return new SimpleStringProperty(order.getValue().getCustomer().getPhoneNumber());
            }
        });
        phone.setMinWidth(100);

        TableColumn<Order, String> emailAddress = new TableColumn<>("Email Address");
        emailAddress.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> order) {
                return new SimpleStringProperty(order.getValue().getCustomer().getEmail());
            }
        });
        emailAddress.setMinWidth(180);

        TableColumn<Order, Integer> countOfGuitars = new TableColumn<>("Count");
        countOfGuitars.setCellValueFactory(new PropertyValueFactory<>("countOfGuitars"));
        countOfGuitars.setMinWidth(100);

        orderTableView.getColumns().addAll(orderID, date, customerName, city, phone, emailAddress, countOfGuitars, totalCost);
        prepareScrollPane();
    }

    //Adding a scroll pane to the view in case user needs one.
    void prepareScrollPane() {
        ScrollPane scPane = new ScrollPane();
        scPane.setContent(orderTableView);
        scPane.setPrefSize(300, 300);
        scPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    //Initializes the table view for guitars.
    void prepareGuitarTableView() {

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

        guitarTableView.getColumns().addAll(quantity, brand, model, type, price);
    }
}
