package Scene;

import Dal.GuitarData;
import Dialogs.WarningDialog;
import Model.Guitar;
import Model.GuitarType;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class StockScene extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    TableView <Guitar> guitars;
    BorderPane pane;
    CheckBox negateBox;
    TextField quantityField;
    public StockScene(BorderPane pane) {
        this.pane = pane;
    }

    @Override
    public void start(Stage stage) {

        stage.setTitle("Guitar FX - Edit Stock");
        guitars = new TableView();

        Label stockLabel = new Label("Stock Maintenance");
        stockLabel.setId("welcomeLabel");
        prepareTableView();

        VBox topContainer = new VBox();
        topContainer.setPadding(new Insets(20,0,0,0));
        topContainer.getChildren().addAll(stockLabel,guitars);
        topContainer.setSpacing(40);

        HBox bottomContainer = new HBox();

        Label quantityInfo = new Label("Please enter quantity:");

        quantityField = new TextField();
        negateBox = new CheckBox("Negate");
        Button addButton= new Button("Add");
        bottomContainer.setSpacing(10);
        bottomContainer.getChildren().addAll(quantityInfo,quantityField,negateBox,addButton);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.setPadding(new Insets(20,0,60,0));
        bottomContainer.setSpacing(20);

        pane.setCenter(topContainer);
        pane.setBottom(bottomContainer);

        fillGuitarTableView();
        GuitarData guitarData= new GuitarData();

        addButton.setOnAction(actionEvent -> {
            addButtonAction(stage);
            fillGuitarTableView(); //For updating
        });
    }

//For shortening the add button setonaction section since the start method was too long.
    void addButtonAction(Stage stage){
        Guitar selectedGuitar = guitars.getSelectionModel().getSelectedItem();
        if(guitars.getSelectionModel().getSelectedItem()!=null){
            ObservableList<ObservableMap.Entry<Guitar,Integer>> guitars = FXCollections.observableArrayList();
            try{
                Integer quantityOfGuitar = Integer.parseInt(quantityField.getText());
                guitars.add(Map.entry(selectedGuitar,quantityOfGuitar));

                if(quantityOfGuitar>0){

                    if(negateBox.isSelected()){
                        if(selectedGuitar.getStock()>=quantityOfGuitar){
                            new GuitarData().updateStockData(guitars,true);
                        }
                        else{
                            WarningDialog warning = new WarningDialog(stage,"Not enough guitars in stock to deduct","Not enough guitars");
                            warning.getStage().showAndWait();
                        }
                    }
                    else{
                        new GuitarData().updateStockData(guitars,false);
                    }
                }
                else{
                    WarningDialog warning = new WarningDialog(stage,"Please only input positive integers","Not a positive integer");
                    warning.getStage().showAndWait();
                }


            }catch (NumberFormatException nfe){
                WarningDialog warning = new WarningDialog(stage,"Please only input integers","Not an integer");
                warning.getStage().showAndWait();
            }
        }
        else{
            WarningDialog warning = new WarningDialog(stage,"Please select a guitar","Guitar not selected");
            warning.getStage().showAndWait();
        }
    }

    //filling the guitar list
    void fillGuitarTableView(){
        guitars.getItems().clear();
        guitars.setItems(FXCollections.observableList(new GuitarData().readGuitars()));
    }

    //Preparing a tableview for  guitarlist
    void prepareTableView(){
        TableColumn<Guitar,String> brand =  new TableColumn<>("Brand");
        brand.setCellValueFactory(new PropertyValueFactory<Guitar,String>("brand"));
        brand.setMinWidth(150);

        TableColumn<Guitar,String> model = new TableColumn<>("Model");
        model.setCellValueFactory(new PropertyValueFactory<Guitar,String >("model"));
        model.setMinWidth(150);

        TableColumn<Guitar,Boolean> isAcoustic = new TableColumn<>("Is Acoustic");
        isAcoustic.setCellValueFactory(new PropertyValueFactory<Guitar,Boolean>("isAcoustic"));
        isAcoustic.setMinWidth(150);

        TableColumn<Guitar, GuitarType> type = new TableColumn<>("Guitar Type");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        type.setMinWidth(150);

        TableColumn<Guitar,Double> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        price.setMinWidth(150);

        TableColumn<Guitar,Integer> stock = new TableColumn<>("Stock");
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stock.setMinWidth(150);

        guitars.getColumns().addAll(brand,model,isAcoustic,type,price,stock);

    }
}
