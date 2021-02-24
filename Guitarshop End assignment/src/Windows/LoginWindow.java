package Windows;

import Dal.EmployeeData;
import Dialogs.WarningDialog;
import Model.Employee;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import nl.inholland.exam.Question1.AccountLockedDialog;

import javax.security.auth.login.AccountLockedException;

public class LoginWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    static int nrOfTries;
    static String previousUsername;
    //METHOD FOR QUESTION 1
    boolean isAccountLocked(String currentUsername){
        if(nrOfTries==3){
            return true;
        }
        else if(currentUsername.equals(previousUsername)){
            nrOfTries++;
            return false;
        }
        else{
            nrOfTries=1;
            previousUsername= currentUsername;
            return false;
        }
    }

    // Preparing the stage for  logging in
    @Override
    public void start(Stage stage) {
        nrOfTries=1;
        previousUsername="";
        stage.setTitle("Login Screen");
        GridPane root = new GridPane();
        stage.setHeight(400);
        stage.setWidth(400);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10,10,10,10));

        Label usernameLbl = new Label("Username:");
        Label passwordLbl = new Label("Password:");
        TextField usernameText = new TextField();
        PasswordField passwordText = new PasswordField();
        Button loginButton = new Button("Login to the system");

        root.add(usernameLbl,0,10);
        root.add(passwordLbl,0,11);
        root.add(usernameText,3,10);
        root.add(passwordText,3,11);
        root.add(loginButton,3,16);

        // Checking the credentials
        EmployeeData data = new EmployeeData();
        loginButton.setOnAction(event -> {
            Employee loggedEmployee = data.verifyCredentials(usernameText.getText(),passwordText.getText());
            if(loggedEmployee!=null){

                EmployeeWindow window = new EmployeeWindow(loggedEmployee,stage);
                window.start(stage);
            }
            else{
                System.out.println(nrOfTries);
                if(isAccountLocked(usernameText.getText())){
                    try {
                        throw new AccountLockedException();
                    } catch (AccountLockedException e) {
                        new AccountLockedDialog(stage).getStage().showAndWait();
                    }
                }
                WarningDialog dialog = new WarningDialog(stage,"Incorrect credentials please try again","Login unsuccessful");
                dialog.getStage().showAndWait();
            }
        });
        Scene scene = new Scene(root);
        scene.getStylesheets().add("Resources/Styles/style.css");
        stage.setScene(scene);
        stage.show();
    }


}
