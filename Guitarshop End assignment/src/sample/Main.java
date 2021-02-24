package sample;
import Windows.LoginWindow;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    Stage stage;

    @Override
    public void start(Stage stage) throws Exception{

        this.stage=stage;
        //Application starts from login window.
        LoginWindow window = new LoginWindow();
        window.start(stage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
