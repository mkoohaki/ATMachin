package sample.comtrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import sample.AccountDatabase;
import sample.Currency;
import sample.Partials;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    @FXML
    private javafx.scene.control.ChoiceBox<String> currency;

    @FXML
    private Text accountNo;

    @FXML
    javafx.scene.control.Button transfer, eTransfer, wdOnClick, logoutOnclick, checkingBalance, savingBalance, lineBalance, newMessage;

    String accountNum, checkingBal, savingBal, lineBal;
    private final String[] currencyType = {"CA $", "$", "£", "¥", "€"};

    void setInfo(String accountNumber) throws SQLException, IOException{

        AccountDatabase db = new AccountDatabase();
        String[] accountInfo = db.login(accountNumber);

        accountNum = accountNumber;
        checkingBal = accountInfo[6];
        savingBal = accountInfo[7];
        lineBal = accountInfo[8];

        accountNo.setText(accountNum);

        checkingBalance.setText(checkingBal);
        savingBalance.setText(savingBal);
        lineBalance.setText(lineBal);

        currency.setValue("CA $");

        boolean transactionInfo = db.newMessages(accountNum);

        if(transactionInfo) {
            newMessage.setText("New e-transaction");
            newMessage.setStyle("-fx-background-color: RED;");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        currency.getItems().addAll(currencyType);
        currency.setOnAction(this::setAmount);
    }

    public void setAmount(ActionEvent event) {

        try {
            Locale country;
            String myCurrency = currency.getValue();
            double rate = Currency.currency(myCurrency);
            switch (myCurrency) {
                case "¥":
                    country = new Locale("jp", "JP");
                    break;
                case "€":
                    country = new Locale("de", "DE");
                    break;
                case "$":
                    country = new Locale("en", "us");
                    break;
                case "£":
                    country = new Locale("en", "gb");
                    break;
                default:
                    country = new Locale("en", "ca");
                    break;
            }
            NumberFormat count = NumberFormat.getCurrencyInstance(country);
            checkingBalance.setText(count.format(Double.parseDouble(checkingBal)*rate));
            savingBalance.setText(count.format(Double.parseDouble(savingBal)*rate));
            lineBalance.setText(count.format(Double.parseDouble(lineBal)*rate));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    private void withDep(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("withdrawDeposit.fxml"));
        Parent root = loader.load();
        WithdrawDepositController withdrawDepositController = loader.getController();
        withdrawDepositController.setInfo(accountNum);
        wdOnClick.getScene().setRoot(root);
    }

    @FXML
    private void eTransfer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("etransfer.fxml"));
        Parent root = loader.load();
        ETransferController etransferController = loader.getController();
        etransferController.setInfo(accountNum);
        eTransfer.getScene().setRoot(root);
    }

    @FXML
    private void transfer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("transfer.fxml"));
        Parent root = loader.load();
        TransferController transferController = loader.getController();
        transferController.setInfo(accountNum);
        transfer.getScene().setRoot(root);
    }

    @FXML
    public void logOut (ActionEvent event) throws IOException {
        try {
            Partials.windowOpen("Login", "Royal Canadian Bank", 600, 400);
            Partials.windowClose(event);
        } catch (Exception e) {
            System.err.println("Cannot clear text field");
        }
    }

    @FXML
    private void checkingClick(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("transactions.fxml"));
        Parent root = loader.load();
        TransactionsController transactionsController = loader.getController();
        transactionsController.setInfo(accountNum, "Checking");
        checkingBalance.getScene().setRoot(root);
    }

    @FXML
    private void savingClick(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("transactions.fxml"));
        Parent root = loader.load();
        TransactionsController transactionsController = loader.getController();
        transactionsController.setInfo(accountNum, "Saving");
        savingBalance.getScene().setRoot(root);
    }

    @FXML
    private void lineClick(ActionEvent event) throws IOException {
    }

    @FXML
    private void message(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("eTransactions.fxml"));
        Parent root = loader.load();
        ETransactionsController messagesController = loader.getController();
        messagesController.setInfo(accountNum);
        newMessage.getScene().setRoot(root);
    }
}
