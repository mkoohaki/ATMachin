package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.text.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    @FXML
    private javafx.scene.control.ChoiceBox<String> currency;

    @FXML
    private Text accountNo, checkingB, savingB, lineB;

    @FXML
    javafx.scene.control.Button tOnClick, wdOnClick, logoutOnclick;

    private final Window window = new Window();
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
        checkingB.setText(Currency.currency("cad", checkingBal));
        savingB.setText(Currency.currency("cad", savingBal));
        lineB.setText(Currency.currency("cad", lineBal));

        currency.setValue("CA $");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        currency.getItems().addAll(currencyType);
        currency.setOnAction(this::setAmount);
    }

    public void setAmount(ActionEvent event) {

        try {
            String myCurrency = currency.getValue();
            checkingB.setText(Currency.currency(myCurrency, checkingBal));
            savingB.setText(Currency.currency(myCurrency, savingBal));
            lineB.setText(Currency.currency(myCurrency, lineBal));
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
    private void transfer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("transfer.fxml"));
        Parent root = loader.load();
        TransferController transferController = loader.getController();
        transferController.setInfo(accountNum);
        wdOnClick.getScene().setRoot(root);
    }

    @FXML
    public void logOut (ActionEvent event) throws IOException {
        try {
            window.open("Login", "Royal Canadian Bank", 600, 400);
            window.close(event);
        } catch (Exception e) {
            System.err.println("Cannot clear text field");
        }
    }
}
