package playing;

import gameplay.Communicable;
import gameplay.Initializer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import playingGame.DisplayData;
import runningGame.GameWindow;

import java.io.File;

public class MainPlayer extends Application {

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("VoogaSalad!");
//        GameWindow myWindow = new GameWindow();
        Group myGroup = new Group();
        primaryStage.setScene(new Scene(myGroup, SCREEN_WIDTH, SCREEN_HEIGHT));
        DisplayData myDisp = new DisplayData(myGroup);

        File file = new File("/");
        Communicable myCommuicator = new Communicator(myDisp);
        Initializer myInit = new Initializer(file, myCommuicator);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
