package Dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WarningDialog {

    //Simple pop up dialog class to inform the user in many ways

    private Stage stage;
    Label infoLabel;

    // pass the info with constructor.
    public WarningDialog(Stage ownerStage, String information, String title) {
        stage= new Stage();
        stage.initOwner(ownerStage);
        stage.initModality(Modality.WINDOW_MODAL);
        infoLabel = new Label(information);
        stage.setTitle(title);
        prepareStage();
    }

    public Stage getStage() {
        return stage;
    }

    private void prepareStage(){


        GridPane root = new GridPane();
        infoLabel.setId("warning");

        root.add(infoLabel,2,2);

        Scene scene = new Scene(root);

        Button okButton = new Button("OK");

        root.setPadding(new Insets(10,10,10,10));
        root.setAlignment(Pos.CENTER);

        okButton.setOnAction(event -> {
            stage.close();
        });
        root.add(okButton,2,4);
        root.setVgap(20);
        okButton.setAlignment(Pos.CENTER);

       scene.getStylesheets().add("Resources/Styles/style.css");
        stage.setHeight(200);
        stage.setWidth(400);
        stage.setScene(scene);

    }

}
