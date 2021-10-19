package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import sample.Partials;

import java.io.IOException;

public class EnterController {

    @FXML
    private void enter(ActionEvent event) throws IOException {
        try {
            Partials.windowOpen("login", "Royal Canadian Bank", 600, 400);
            Partials.windowClose(event);
        } catch (Exception e) {
            System.err.println("Cannot load file!");
        }
    }
}
