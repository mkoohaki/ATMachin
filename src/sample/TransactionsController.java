package sample.comtrollers;

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
import sample.AccountDatabase;
import sample.ModelTable;

import java.io.IOException;
import java.sql.SQLException;

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

        AccountDatabase db = new AccountDatabase();
        String[] accountInfo = db.login(accountNum);

        transactionsList = db.activity(accountNum, account);
        eTransactionsList = db.eTransactionsInTransactions(accountNum, account);

        if(account.equals("Checking")) {
            balance.setText(accountInfo[6]);
            title.setText("Chequing Transactions");
        }
        else if(account.equals("Saving")) {
            balance.setText(accountInfo[7]);
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
