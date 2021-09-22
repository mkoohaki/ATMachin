package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WithdrawDepositController implements Initializable {

    @FXML
    private javafx.scene.control.ChoiceBox<String> transaction, account;

    @FXML
    javafx.scene.control.TextField amount;

    @FXML
    javafx.scene.control.Button onClick;

    @FXML
    Text message;

    private Window window = new Window();
    private final String[] transactionType = {"Select", "Deposit", "Withdraw"};
    private final String[] accounts = {"Select", "Checking", "Saving"};

    String accountNum;

    void setInfo(String accountNumber){

        accountNum = accountNumber;
        message.setText("");

        transaction.setValue("Select");
        account.setValue("Select");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        transaction.getItems().addAll(transactionType);
        account.getItems().addAll(accounts);
    }

    @FXML
    private void transact(ActionEvent event) throws SQLException {
        message.setText("");
        AccountDatabase db = new AccountDatabase();
        String[] accountInfo = db.login(accountNum);
        boolean done = false;
        String checkingBal = accountInfo[6], savingBal = accountInfo[7], lineBal = accountInfo[8];
        double checkBalance = Double.parseDouble(checkingBal),
               saveBalance = Double.parseDouble(savingBal),
               amnt = Double.parseDouble(amount.getText());

        String trans = transaction.getValue();
        String acc = account.getValue();

        if (trans.equals("Withdraw")) {
            if (acc.equals("Checking")) {
                if (checkBalance >= amnt) {
                    checkBalance -= amnt;
                    done = true;
                }
            } else if (acc.equals("Saving")) {
                if (saveBalance >= amnt) {
                    saveBalance -= amnt;
                    done = true;
                }
            }
        } else if (trans.equals("Deposit")) {
            switch(acc) {
                case "Checking":
                    checkBalance += amnt;
                    break;
                case "Saving":
                    saveBalance += amnt;
                    break;
            }
            done = true;
        }

        db.updateRow(accountNum, String.valueOf(checkBalance), String.valueOf(saveBalance), lineBal);
        if (done) {
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Transaction done successfully");
            alert.showAndWait();
        } else {
            message.setText("Amount is larger than balance!");
        }
        transaction.getSelectionModel().selectFirst();
        account.getSelectionModel().selectFirst();
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
