package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class TransferController implements Initializable {

    @FXML
    private javafx.scene.control.ChoiceBox<String> accountFrom, accountTo;

    @FXML
    javafx.scene.control.TextField amount;

    @FXML
    javafx.scene.control.Button onClick;

    @FXML
    Text message;

    private Window window = new Window();
    private final String[] accounts = {"Select", "Checking", "Saving", "Line of credit"};

    String accountNum;

    void setInfo(String accountNumber) {

        accountNum = accountNumber;
        message.setText("");

        accountFrom.setValue("Select");
        accountTo.setValue("Select");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        accountFrom.getItems().addAll(accounts);
        accountTo.getItems().addAll(accounts);
    }

    @FXML
    private void transfer(ActionEvent event) throws SQLException {

        message.setText("");
        AccountDatabase db = new AccountDatabase();
        String[] accountInfo = db.login(accountNum);
        boolean done = false;
        String checkingBal = accountInfo[6], savingBal = accountInfo[7], lineBal = accountInfo[8];
        double checkBalance = Double.parseDouble(checkingBal),
               saveBalance = Double.parseDouble(savingBal),
               lineBalance = Double.parseDouble(lineBal),
               amnt = Double.parseDouble(amount.getText());

        String accountF = accountFrom.getValue();
        String accountT = accountTo.getValue();

        switch (accountF) {
            case "Checking":
                if (checkBalance >= amnt) {
                    checkBalance -= amnt;
                    switch (accountT) {
                        case "Saving":
                            saveBalance += amnt;
                        case "Line of credit":
                            lineBalance += amnt;
                    }
                    done = true;
                }
                break;
            case "Saving":
                if (saveBalance >= amnt) {
                    saveBalance -= amnt;
                    switch (accountT) {
                        case "Checking":
                            checkBalance += amnt;
                        case "Line of credit":
                            lineBalance += amnt;
                    }
                    done = true;
                }
                break;
            case "Line of credit":
                if (lineBalance >= amnt) {
                    lineBalance -= amnt;
                    switch (accountT) {
                        case "Checking":
                            checkBalance += amnt;
                        case "Saving":
                            saveBalance += amnt;
                    }
                    done = true;
                }
                break;
        }

        db.updateRow(accountNum, String.valueOf(checkBalance), String.valueOf(saveBalance), String.valueOf(lineBalance));
        if (done) {
            javafx.scene.control.Alert alert;
            alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setContentText("Transaction done successfully");
            alert.showAndWait();
        } else {
            message.setText("Amount is larger than balance!");
        }
        accountFrom.getSelectionModel().selectFirst();
        accountTo.getSelectionModel().selectFirst();
        amount.setText("");
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("account.fxml"));
            Parent root = loader.load();
            AccountController accountController = loader.getController();
            accountController.setInfo(accountNum);
            onClick.getScene().setRoot(root);
        } catch (Exception e) {
            System.err.println("Cannot load file!");
        }
    }
}
