package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.database.AccountDatabase;

import java.sql.SQLException;
import java.util.Objects;
import java.security.SecureRandom;

public class Partials {

    private static final int PHONE_AND_ACCOUNT_DIGIT_NUMBER = 10;

    public static void windowOpen(String fileName, String title, int width, int height) throws Exception {

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(Partials.class.getResource("interfaces/" + fileName + ".fxml")));
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, height, width));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void windowClose(ActionEvent event) {

        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public static boolean isValidNumber(String number) {
        String allNumbers = "0123456789";
        int numbers = 0;

        if(number.length() == PHONE_AND_ACCOUNT_DIGIT_NUMBER) {
            char[] ch = new char[number.length()];
            for (int i = 0; i < number.length(); i++) {
                ch[i] = number.charAt(i);
                if (allNumbers.contains(Character.toString(ch[i])))
                    numbers++;
                else
                    return false;
            }
            return numbers == PHONE_AND_ACCOUNT_DIGIT_NUMBER;
        }
        return false;
    }

    public static boolean isNumber(String number) {
        String allNumbers = "0123456789";

        char[] ch = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            ch[i] = number.charAt(i);
            if (!allNumbers.contains(Character.toString(ch[i])))
                return false;
        }
        return true;
    }

    public static void alert(String message, String kindOfAlert) {
        try {
            javafx.scene.control.Alert alert;
            if(kindOfAlert.equals("error")) {
                alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Error");
            } else {
                alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Successful signup");
            }
            alert.setContentText(message);
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Cannot load file!");
        }
    }

    public static boolean isValidAccount(String accountNumber) throws SQLException {

        AccountDatabase db = new AccountDatabase();
        String[] accountInfo = db.login(accountNumber);
        return accountInfo[0] != null;
    }

    public static String activationCode() {

        SecureRandom rnd = new SecureRandom();
        int randomNumber = rnd.nextInt(999999);
        return String. format("%06d", randomNumber);
    }
}
