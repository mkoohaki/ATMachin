package sample.partials;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import sample.Partials;
import sample.database.AccountDatabase;
import sample.interfaces.AccountSelectionController;

import java.sql.SQLException;

public class ModelTable {

    private String account_from, amount, date, account_to, depositedAccount, confirm;
    private Button button;

    AccountDatabase db = new AccountDatabase();

    public ModelTable(String account_from, String amount, String date) throws SQLException {

        this.account_from = account_from;
        this.amount = amount;
        this.date = date;
    }

    public ModelTable(String account_from, String account_to, String amount, String date, String confirm) throws SQLException {

        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
        this.date = date;
        this.confirm = confirm;

        setButton(confirm);
    }

    public String getDepositedAccount() {

        return depositedAccount;
    }

    public void setDepositedAccount(String depositedAccount) {

        this.depositedAccount = depositedAccount;
    }

    public String getConfirm() {

        return confirm;
    }

    public void setConfirm(String confirm) {

        this.confirm = confirm;
    }

    public Button getButton() {

        return button;
    }

    public void setButton(String confirm) {

        switch (confirm) {
            case "cancel":
                this.button = new Button("Cancel");
                button.setId("cancel");
                button.setOnAction(this::cancelTransaction);
                break;
            case "confirm":
                this.button = new Button("Confirm");
                button.setId("confirmButton");
                button.setOnAction(this::confirmTransaction);
                break;
            case "canceled":
                this.button = new Button("Canceled");
                button.setStyle("-fx-background-color: RED; -fx-text-fill: WHITE;");
                button.setDisable(true);
                break;
            case "confirmed":
                this.button = new Button("Confirmed");
                button.setStyle("-fx-background-color: GREEN; -fx-text-fill: WHITE;");
                button.setDisable(true);
                break;
        }
    }

    public void cancelTransaction(ActionEvent actionEvent) {

        String accountNumber, checkingBal, savingBal, lineBal;
        double checkBalance, amount;

        try {
            accountNumber = getAccount_from();
            String[] accountInfo = db.login(accountNumber);

            checkingBal = accountInfo[6];
            savingBal = accountInfo[7];
            lineBal = accountInfo[8];

            checkBalance = Double.parseDouble(checkingBal);
            amount = Double.parseDouble(getAmount());

            checkBalance += amount;

            db.updateRow(accountNumber, String.valueOf(checkBalance), savingBal, lineBal);

            db.updateETransaction("Canceled", null, getAccount_from(), getAccount_to(), getAmount(), getDate());
            Partials.alert("Transaction is canceled successfully", "Notification");

            button.setText("Canceled");
            button.setStyle("-fx-background-color: RED; -fx-text-fill: WHITE;");
            button.setDisable(true);
        } catch (SQLException  e) {
            System.out.println(e);
        }
    }

    public void confirmTransaction(ActionEvent actionEvent) {
        try {
            Partials.windowOpen("accountSelection", "Royal Canadian Bank", 250, 400);
            AccountSelectionController accountSelectionController = new AccountSelectionController();
            accountSelectionController.setInfo(account_from, account_to, amount, date);

            button.setText("Confirmed");
            button.setStyle("-fx-background-color: GREEN; -fx-text-fill: WHITE;");
            button.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAccount_from() {
        return account_from;
    }

    public String getAccount_to() {
        return account_to;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }
}

