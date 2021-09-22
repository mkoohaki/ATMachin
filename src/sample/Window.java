package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window {


    public void open(String fileName, String title, int width, int height) throws Exception {

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(fileName + ".fxml"));
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, height, width));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void close(ActionEvent event) {

        ((Node)(event.getSource())).getScene().getWindow().hide();

    }
}
