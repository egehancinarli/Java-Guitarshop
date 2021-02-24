package nl.inholland.exam.Question1;

import com.sun.media.jfxmediaimpl.platform.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AccountLockedDialog {

    //IMPORTANT NOTE: PLEASE CHECK LOGIN SCREEN, ACCOUNTLOCKED EXCEPTIONS ASWELL FOR THIS QUESTION
    private Stage stage;
    Stage ownerStage;

    public Stage getStage() { return stage; }

    public AccountLockedDialog(Stage ownerStage) {
        stage= new Stage();
        this.ownerStage=ownerStage;
        stage.initOwner(ownerStage);
        stage.initModality(Modality.WINDOW_MODAL);
        prepareStage();
    }
    void prepareStage(){
        stage.setTitle("Account is locked");
        GridPane root = new GridPane();
        Label infoLabel = new Label("Due to entering wrong credentials three times in a row \nYour account has been locked");
        infoLabel.setId("warning");

        root.add(infoLabel,2,2);


        Button okButton = new Button("OK");

        root.setPadding(new Insets(10,10,10,10));
        root.setAlignment(Pos.CENTER);

        okButton.setOnAction(event -> {
           Runtime.getRuntime().exit(0);
        });
        root.add(okButton,2,4);
        root.setVgap(20);
        okButton.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);

        scene.getStylesheets().add("Resources/Styles/style.css");
        stage.setHeight(300);
        stage.setWidth(500);
        stage.setScene(scene);
    }
}
