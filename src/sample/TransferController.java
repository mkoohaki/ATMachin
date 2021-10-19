package sample.comtrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import sample.AccountDatabase;
import sample.Partials;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TransferController implements Initializable {

    @FXML
    private javafx.scene.control.ChoiceBox<String> accountFrom, accountTo;

    @FXML
    javafx.scene.control.TextField accountNo, amount;

    @FXML
    javafx.scene.control.Button onClick;

    @FXML
    Text message;

    private final String[] accountF = {"Select", "Checking", "Saving", "Line of credit"};
    private final String[] accountT = {"Select", "Checking", "Saving", "Line of credit"};

    String accountNum;

    void setInfo(String accountNumber) {

        accountNum = accountNumber;
        message.setText("");

        accountFrom.setValue(accountF[0]);
        accountTo.setValue(accountT[0]);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        accountFrom.getItems().addAll(accountF);
        accountTo.getItems().addAll(accountT);
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

        String msg = null;

        if (accountF.equals("Checking") && !accountT.equals("Checking")) {
            if (checkBalance >= amnt) {
                checkBalance -= amnt;
                switch (accountT) {
                    case "Saving":
                        saveBalance += amnt;
                    case "Line of credit":
                        lineBalance += amnt;
                }
                done = true;
            } else {
                msg = "Amount is larger than balance!";
            }
        } else if (accountF.equals("Saving") && !accountT.equals("Saving")) {
            if (saveBalance >= amnt) {
                saveBalance -= amnt;
                switch (accountT) {
                    case "Checking":
                        checkBalance += amnt;
                    case "Line of credit":
                        lineBalance += amnt;
                }
                done = true;
            } else {
                msg = "Amount is larger than balance!";
            }
        } else if (accountF.equals("Line of credit") && !accountT.equals("Line of credit")) {
            if (lineBalance >= amnt) {
                lineBalance -= amnt;
                switch (accountT) {
                    case "Checking":
                        checkBalance += amnt;
                    case "Saving":
                        saveBalance += amnt;
                }
                done = true;
            } else {
                msg = "Amount is larger than balance!";
            }
        } else {
            msg = "Error- Select different accounts!";
        }


        if (done) {
            db.updateRow(accountNum, String.valueOf(checkBalance), String.valueOf(saveBalance), String.valueOf(lineBalance));
            db.insertActivityRow(accountNum, "Transfer", accountF, accountT, String.valueOf(amnt));
            Partials.alert("Transaction done successfully", "Notification");
        } else {
            message.setText(msg);
        }
        accountFrom.getSelectionModel().selectFirst();
        accountTo.getSelectionModel().selectFirst();
        amount.setText("");
    }

    @FXML
    private void back(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("account.fxml"));
        Parent root = loader.load();
        AccountController accountController = loader.getController();
        accountController.setInfo(accountNum);
        onClick.getScene().setRoot(root);
    }
}
