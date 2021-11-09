package sample.interfaces;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import sample.Partials;
import sample.database.AccountDatabase;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AccountSelectionController implements Initializable {

    @FXML
    private javafx.scene.control.ChoiceBox<String> account;

    @FXML
    javafx.scene.control.Button onClick, back;

    private final String[] accounts = {"Select", "Checking", "Saving"};
    static String account_from, account_to, date;
    static double amount;

    public void setInfo(String accountFrom, String accountTo, String amnt, String time){

        account_from = accountFrom;
        account_to = accountTo;
        amount = Double.parseDouble(amnt);
        date = time;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        account.setValue(accounts[0]);
        account.getItems().addAll(accounts);
    }

    @FXML
    private void deposit(ActionEvent event) throws SQLException {

        String checkingBal, savingBal, lineBal;
        double checkingBalance, savingBalance;
        AccountDatabase db = new AccountDatabase();

        String[] accountInfo = db.login(account_to);

        checkingBal = accountInfo[6];
        savingBal = accountInfo[7];
        lineBal = accountInfo[8];

        checkingBalance = Double.parseDouble(checkingBal);
        savingBalance = Double.parseDouble(savingBal);

        String acc = account.getValue();

        if(!acc.equals("Select")) {
            if (acc.equals("Checking")) {
                checkingBalance += amount;
            } else if (acc.equals("Saving")) {
                savingBalance += amount;
            }
            db.updateRow(account_to, String.valueOf(checkingBalance), String.valueOf(savingBalance), lineBal);
            db.updateETransaction("Confirmed", acc, account_from, account_to, String.valueOf(amount), date);
            Partials.windowClose(event);
            Partials.alert("Transaction done successfully", "Notification");
        }
    }

    @FXML
    private void close(ActionEvent event) {
        try {
            Partials.windowClose(event);
        } catch (Exception e) {
            System.err.println("Cannot load file!");
        }
    }
}
