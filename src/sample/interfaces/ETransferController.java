package sample.interfaces;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import sample.database.AccountDatabase;
import sample.Partials;
import sample.partials.SendEmail;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ETransferController implements Initializable {

    @FXML
    private javafx.scene.control.ChoiceBox<String> accountFrom;

    @FXML
    javafx.scene.control.TextField accountNo, amount;

    @FXML
    javafx.scene.control.Button onClick, backClick;

    @FXML
    Text message;

    private final String[] account = {"Select", "Checking"};

    String account_from, account_to;

    void setInfo(String accountNumber) {

        account_from = accountNumber;
        message.setText("");
        accountFrom.setValue("Select");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        accountFrom.getItems().addAll(account);
    }

    @FXML
    private void transfer(ActionEvent event) throws SQLException {

        message.setText("");
        account_to = accountNo.getText();
        AccountDatabase db = new AccountDatabase();

        String[] accountInfo_from = db.login(account_from);
        String checkingBal_from = accountInfo_from[6], savingBal_from = accountInfo_from[7], lineBal_from = accountInfo_from[8];
        double checkBalance_from = Double.parseDouble(checkingBal_from);
        String accountF = accountFrom.getValue();

        if (accountF.equals("Checking")) {
            if(Partials.isValidNumber(account_to)) {
                if(Partials.isValidAccount(account_to)) {
                    if(!Objects.equals(account_to, account_from)) {
                        if(Partials.isNumber(amount.getText())) {
                            double amnt = Double.parseDouble(amount.getText());
                            if (checkBalance_from >= amnt) {
                                checkBalance_from -= amnt;
                                String[] accountInfo_to = db.login(account_to);
                                db.updateRow(account_from, String.valueOf(checkBalance_from), savingBal_from, lineBal_from);
                                db.insertRowTransaction("Not Confirmed", account_from, account_to, String.valueOf(amnt));
                                Partials.alert("Transaction done successfully", "notification");
                                SendEmail.mailing(accountInfo_to[4], accountInfo_to[3], "", accountInfo_from[3],
                                        String.valueOf(amnt), "etransfer");
                            } else {
                                message.setText("Amount is larger than balance!");
                            }
                        } else {
                            message.setText("Please inter correct amount");
                        }
                    } else {
                        message.setText("Account number is wrong");
                        account_to = "";
                        accountNo.setText("");
                    }
                } else {
                    message.setText("Account number is wrong");
                    account_to = "";
                    accountNo.setText("");
                }
            } else {
                message.setText("Account number is required, it is a 10 digits number");
                account_to = "";
                amount.setText("");
                accountNo.setText("");
            }
        } else {
            message.setText("Please select account");
        }
        accountFrom.getSelectionModel().selectFirst();
        amount.setText("");
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("account.fxml"));
            Parent root = loader.load();
            AccountController accountController = loader.getController();
            accountController.setInfo(account_from);
            backClick.getScene().setRoot(root);
        } catch (Exception e) {
            System.err.println("Cannot load file! " + e);
        }
    }

//    public void confirm(String account_to, double amount) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("accountSelection.fxml"));
//            Parent root = loader.load();
//            AccountSelectionController accountSelectionController = loader.getController();
//            accountSelectionController.setInfo(account_to, amount);
//            confirm.getScene().setRoot(root);
//        } catch (Exception e) {
//            System.err.println("Cannot load file! " + e);
//        }
//    }
}
