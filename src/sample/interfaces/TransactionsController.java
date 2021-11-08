package sample.interfaces;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import sample.database.AccountDatabase;
import sample.partials.ModelTable;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

public class TransactionsController {

    @FXML
    javafx.scene.control.Button onClick;

    @FXML
    private TableView<Object> table;

    @FXML
    private TableColumn<ModelTable, String> col_activity, col_amount, col_date;

    @FXML
    Text balance;

    @FXML
    Label title;

    ObservableList transactionsList = FXCollections.observableArrayList();
    ObservableList eTransactionsList = FXCollections.observableArrayList();

    String accountNum;

    void setInfo(String accountNumber, String account) throws SQLException, IOException {

        accountNum = accountNumber;
        Locale country =  new Locale("en", "ca");
        NumberFormat count = NumberFormat.getCurrencyInstance(country);

        AccountDatabase db = new AccountDatabase();
        String[] accountInfo = db.login(accountNum);

        transactionsList = db.activity(accountNum, account);
        if(account.equals("Saving"))
            eTransactionsList = db.eTransactionsInTransactionsSaving(accountNum);
        else
            eTransactionsList = db.eTransactionsInTransactionsChecking(accountNum);

        if(account.equals("Checking")) {
            balance.setText(count.format(Double.parseDouble(accountInfo[6])));
            title.setText("Chequing Transactions");
        }
        else if(account.equals("Saving")) {
            balance.setText(count.format(Double.parseDouble(accountInfo[7])));
            title.setText("Saving Transactions");
        }

        col_activity.setCellValueFactory(new PropertyValueFactory<>("account_from"));
        col_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.setItems(transactionsList);
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
