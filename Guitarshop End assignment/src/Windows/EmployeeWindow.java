package Windows;

import Dal.WelcomeMessageData;
import Model.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import Scene.*;
import nl.inholland.exam.WelcomeMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmployeeWindow extends Application {

    Employee loggedEmployee;
    Stage stage;

    public EmployeeWindow(Employee loggedEmployee, Stage stage) {
        this.loggedEmployee = loggedEmployee;
        this.stage = stage;
    }

    public static void main(String[] args) {
        launch(args);
    }


    //METHOD FOR QUESTION 2:
  private   VBox getMessagesInVBox(){
        List<WelcomeMessage> allMessages = new WelcomeMessageData().readMessages();
        VBox generalContainer = new VBox();
        generalContainer.setSpacing(15);

        for (WelcomeMessage message: allMessages){
            Label header=  new Label(message.getTitle());
            Label content = new Label(addLinesToContent(message.getContent()));
            content.setId("contentLabel");
            generalContainer.getChildren().addAll(header,content);
        }
        return generalContainer;
    }
 // METHOD FOR QUESTION 2
   private String addLinesToContent(String content){
        int counter=0;
        //132 is the maximum numbers of characters can be in one line with 1000 width screen and 15 pixel font size.
        String newContent="";
        for (char c : content.toCharArray()){
            if(counter==130){
                counter=0;
                newContent+="\n";
            }
            else{
                counter++;
            }
            newContent+=c;
        }
        return newContent;
    }

    //Apologize for the long method but it is hard to divide it because of the setonaction methods.
    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        stage.setTitle("Guitar FX- Homepage");
        stage.setWidth(1000);
        stage.setHeight(800);

        MenuBar bar = new MenuBar();
        Menu home = new Menu("Home");
        MenuItem homePage = new Menu("HomePage");
        home.getItems().add(homePage);

        Menu account = new Menu("Account");
        MenuItem logOut = new MenuItem("Log out");
        account.getItems().add(logOut);

        Menu sales = new Menu("Sales");
        MenuItem orderAdd = new Menu("Add Orders");
        MenuItem orderView = new Menu("View Orders");

        Menu stock = new Menu("Stock");
        MenuItem showStock = new MenuItem("Edit Stock");
        stock.getItems().add(showStock);

        if (loggedEmployee.getAccessLevel() == AccessLevel.Sales) {
            sales.getItems().addAll(orderAdd, orderView);
        } else {
            sales.getItems().addAll(orderView);
            bar.getMenus().add(stock);
        }
        bar.getMenus().addAll(sales, account);
        //Home is always the first menuitem
        bar.getMenus().add(0, home);

        BorderPane root = new BorderPane();
        root.setTop(bar);

        //Shows home page as default
        showHomePage(root);

        homePage.setOnAction(event -> {
            showHomePage(root);
        });
        orderView.setOnAction(event -> {
            showListOrderPage(root);
        });

        //Opens a scene for adding an order
        orderAdd.setOnAction(event -> {
            showSalesPage(root);
        });

        //Opens a scene for showing stock
        showStock.setOnAction(event -> {
            showStocksPage(root);
        });

        logOut.setOnAction(event -> {
            LoginWindow window = new LoginWindow();
            window.start(stage);
        });

        Scene scene = new Scene(root);

        scene.getStylesheets().add("Resources/Styles/style.css");
        stage.setScene(scene);
        stage.show();

    }

    void clearBorderPane(BorderPane pane) {
        //Clear everything except the navbar section
        pane.setLeft(null);
        pane.setCenter(null);
        pane.setRight(null);
        pane.setBottom(null);
    }

    //A method for showing the home page
    void showHomePage(BorderPane pane) {
        clearBorderPane(pane);

        stage.setTitle("Guitar FX- Homepage");
        GridPane gridPane = new GridPane();
        Label welcomeLabel = new Label(String.format("Welcome %s", loggedEmployee.toString()));
        welcomeLabel.setId("welcomeLabel");
        GridPane.setConstraints(welcomeLabel, 0, 0);

        LocalDateTime date = LocalDateTime.now();
        String formattedTime = String.format("%s / %s:%s", date.toLocalDate(), date.toLocalTime().getHour(), date.toLocalTime().getMinute());
        Label info = new Label(String.format("Your role is : %s \nToday is: %s", loggedEmployee.getAccessLevel().toString(), formattedTime));
        GridPane.setConstraints(info, 0, 2);
        gridPane.getChildren().addAll(welcomeLabel, info);

        gridPane.add(getMessagesInVBox(),0,3);

        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(15, 0, 0, 0));
        pane.setLeft(gridPane);
    }



    //A method for changing the scene to stock page
    void showStocksPage(BorderPane pane) {
        clearBorderPane(pane);
        StockScene window = new StockScene(pane);
        window.start(stage);
    }

    //A method for changing the scene to list order page
    void showListOrderPage(BorderPane pane) {
        clearBorderPane(pane);
        ListOrderScene window = new ListOrderScene(pane);
        window.start(stage);
    }

    //A method for changing the scene to adding order page.
    void showSalesPage(BorderPane pane) {
        clearBorderPane(pane);
        SalesScene window = new SalesScene(pane);
        window.start(stage);
    }
}
