package Dialogs;
import Dal.OrderData;
import Model.Customer;
import Model.Guitar;

import Model.Order;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Map;

public class ConfirmOrderDialog {

    // global variables
    private Stage stage;
    private Order order;
    private final TableView<ObservableMap.Entry<Guitar,Integer>> guitarTableView = new TableView<>();
    private boolean isConfirmed;

    public ConfirmOrderDialog(Stage ownerStage, Order order) {
        this.stage = new Stage();
        this.order = order;
        stage.initOwner(ownerStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Guitar FX - Confirm order");
        isConfirmed=false;
        prepareStage();
    }

    public boolean isConfirmed() { return isConfirmed; }

    public Stage getStage() {
        return stage;
    }


    //This method is used for creating the objects for the dialog
    void prepareStage(){
        stage.setWidth(850);
        stage.setHeight(800);
        stage.setResizable(false);

        Customer customer = order.getCustomer();
        Label customerLbl = new Label(String.format("Customer : %s \n%s\n%s\n%s\n%s",customer.getFullName(),customer.getEmail(),customer.getPhoneNumber(),customer.getStreetAddress(),customer.getCity()));
        prepareTableView();

        Label totalPrice = new Label(String.format("Total Price: %s",order.getTotalCost()));
        Button confirmButton = new Button("Confirm");

        VBox container = new VBox();
        container.setPadding(new Insets(20,20,20,20));
        container.setSpacing(25);
        HBox buttons = new HBox();
        buttons.setSpacing(25);

        Button cancelBtn = new Button("Cancel");
        buttons.getChildren().addAll(confirmButton,cancelBtn);
        buttons.setAlignment(Pos.CENTER);
        container.getChildren().addAll(customerLbl, guitarTableView,totalPrice,buttons);
        addGuitarsToView();

        confirmButton.setOnAction(e->{
            new OrderData().writeOrder(order);
            isConfirmed=true;
            stage.close();
        });

        cancelBtn.setOnAction(e->{
            isConfirmed=false;
            stage.close();
        });

        Scene scene= new Scene(container);
        scene.getStylesheets().add("Resources/Styles/style.css");
        stage.setScene(scene);
    }

//For adding guitars to the guitar table view
    void addGuitarsToView(){
        ObservableList<ObservableMap.Entry<Guitar,Integer>> allGuitars = FXCollections.observableArrayList();
       for (Map.Entry<Guitar,Integer> entry : order.getSelectedGuitars().entrySet()){
           allGuitars.add(Map.entry(entry.getKey(),entry.getValue()));
       }
        guitarTableView.setItems(allGuitars);
    }

    //For preparing the table view.
    void prepareTableView(){
        TableColumn<ObservableMap.Entry<Guitar,Integer> ,String> quantity = new TableColumn<>("Quantity");
        quantity.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String> cell) {
                return new SimpleStringProperty(cell.getValue().getValue().toString());
            }
        });
        quantity.setMinWidth(150);


        TableColumn <ObservableMap.Entry<Guitar,Integer>,String> brand = new TableColumn<>("Brand");
        brand.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String> cell) {
                return new SimpleStringProperty(cell.getValue().getKey().getBrand());
            }
        });
        brand.setMinWidth(150);

        TableColumn<ObservableMap.Entry<Guitar,Integer>,String> model = new TableColumn<>("Model");
        model.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String> cell) {
                return new SimpleStringProperty(cell.getValue().getKey().getModel());
            }
        });
        model.setMinWidth(150);

        TableColumn<ObservableMap.Entry<Guitar,Integer>,String>type = new TableColumn<>("Type");
        type.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String> cell) {
                return new SimpleStringProperty(cell.getValue().getKey().getType().toString());
            }
        });
            type.setMinWidth(150);

        TableColumn<ObservableMap.Entry<Guitar,Integer>,String >price = new TableColumn<>("Price");
        price.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Guitar, Integer>, String> cell) {
                return new SimpleStringProperty(String.valueOf(cell.getValue().getKey().getPrice()));
            }
        });
        price.setMinWidth(150);

        guitarTableView.getColumns().addAll(quantity,brand,model,type,price);
    }
}
