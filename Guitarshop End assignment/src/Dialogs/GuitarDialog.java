package Dialogs;

import Dal.GuitarData;
import Model.Guitar;
import Model.GuitarType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GuitarDialog {

    //these fields should be reachable for other methods aswell
    private Stage stage;
    private Guitar selectedGuitar;
    private Integer quantity;
    private TextField stockField;
    private TableView<Guitar> guitarTableView;
    private Label warningLabel;

    public Guitar getSelectedGuitar() {
        return selectedGuitar;
    }

    public Integer getQuantity (){
        return quantity;
    }

    public Stage getStage() {      return stage;     }

    public GuitarDialog(Stage ownerStage) {
        this.stage = new Stage();
        stage.initOwner(ownerStage);
        stage.initModality(Modality.WINDOW_MODAL);
        prepareStage();
    }

    void prepareStage(){
        BorderPane root = new BorderPane();
        stage.setResizable(false);

        Label welcomeLabel = new Label("Welcome!! Please pick an article");
        welcomeLabel.setId("welcomeLabel");
        root.setTop(welcomeLabel);
        stage.setTitle("Guitar FX- Select an article");
        stage.centerOnScreen();
        stage.setWidth(850);
        stage.setHeight(600);
        guitarTableView= new TableView<>();
        prepareTableView(guitarTableView);

        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");
        stockField = new TextField();
        buttonLayout.getChildren().addAll(stockField,addButton,cancelButton);
        buttonLayout.setAlignment(Pos.CENTER);

        VBox container = new VBox();
        container.setSpacing(10);
        warningLabel = new Label();
        warningLabel.setId("warningLabel");
        container.getChildren().addAll(buttonLayout,warningLabel);

        addButton.setOnAction(actionEvent -> {
        addButtonAction();
        });

        cancelButton.setOnAction(actionEvent -> {
            stage.close();
        });

        root.setPadding(new Insets(20,20,20,20));
        root.setBottom(container);
        root.setCenter(guitarTableView);
        addGuitarsToList(guitarTableView);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("Resources/Styles/style.css");
        stage.setScene(scene);

    }

    // happens when clicked to addbutton
    void addButtonAction(){
        Guitar guitar= (Guitar) guitarTableView.getSelectionModel().getSelectedItem();
        if(guitarTableView.getSelectionModel().getSelectedItem()!=null){
            try{
                Integer quantityOfGuitar = Integer.parseInt(stockField.getText());

                if(quantityOfGuitar>guitar.getStock()){
                    warningLabel.setText(String.format(" There is only  %d  %s in the stock right now, please enter a valid stock",guitar.getStock(),guitar.getModel()));
                }
                else {
                    if (quantityOfGuitar > 0) {
                        selectedGuitar = guitar;
                        quantity = quantityOfGuitar;
                        stage.close();
                    }
                    else{
                        warningLabel.setText("Please only enter positive integers");
                    }
                }
            }catch (NumberFormatException nfe){
                warningLabel.setText("Please only enter integers to this field");
            }
        }
        else{
            warningLabel.setText("Please pick a guitar");
        }
    }

    //For preparing the guitar table view
    void prepareTableView(TableView<Guitar> guitarTableView){


        TableColumn<Guitar,String> brand = new TableColumn<>("Brand");
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        brand.setMinWidth(100);

        TableColumn<Guitar,String> model = new TableColumn<>("model");
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        model.setMinWidth(100);

        TableColumn<Guitar,Boolean> isAcoustic = new TableColumn<>("Acoustic");
        isAcoustic.setCellValueFactory(new PropertyValueFactory<>("isAcoustic"));
        isAcoustic.setMinWidth(100);

        TableColumn<Guitar,GuitarType> type = new TableColumn<>("Guitar Type");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        type.setMinWidth(100);

        TableColumn<Guitar,Double> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        price.setMinWidth(100);

        TableColumn<Guitar,Integer> stock = new TableColumn<>("Stock");
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stock.setMinWidth(100);

        guitarTableView.getColumns().addAll(brand,model,isAcoustic,type,price,stock);
    }

    //For adding guitars to the list.
    void addGuitarsToList(TableView<Guitar> guitarTable){

        ArrayList<Guitar> guitars = new GuitarData().readGuitars();
        ObservableList<Guitar>guitarList = FXCollections.observableArrayList(guitars);
        guitarTable.setItems(guitarList);

    }
}
