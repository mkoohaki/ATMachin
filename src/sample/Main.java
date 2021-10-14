package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private final long createdMillis = System.currentTimeMillis();

    @Override
    public void start(Stage primaryStage) throws Exception {

        Partials.windowOpen("welcome", "Royal Canadian Bank", 600, 400);
    }
}
