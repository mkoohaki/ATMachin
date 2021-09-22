package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class EnterController {

    private Window window = new Window();

    @FXML
    private void enter(ActionEvent event) throws IOException {
        try {
            window.open("login", "Royal Canadian Bank", 600, 400);
            window.close(event);
        } catch (Exception e) {
            System.err.println("Cannot load file!");
        }
    }
}
