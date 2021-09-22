package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import javafx.event.ActionEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    private Window window = new Window();

    public static void main(String[] args) {
        launch(args);
    }
    private final long createdMillis = System.currentTimeMillis();

    @Override
    public void start(Stage primaryStage) throws Exception {

        window.open("welcome", "Royal Canadian Bank", 600, 400);
    }
}
