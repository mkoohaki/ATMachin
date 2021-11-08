package sample;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Database {

    private String username = "root", password ="";
    private String host = "localhost";
    private int port = 3306;
    private String database = "atm_database";
    private String connectionString = "jdbc:mysql://"+ host + ":" + port
            + "/"+database;
    private Connection connection;

    public Database() throws SQLException {

        connection = DriverManager.getConnection(connectionString, username, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public abstract String[] getRow(String pkId) throws SQLException;
    public abstract String[][] getAllRows() throws SQLException;
    public abstract String[] getAllColumns() throws SQLException;
    public abstract String[] login(String accountNumber) throws SQLException;
    public abstract String[] email(String email) throws SQLException;
    public abstract void updateRow(String pkId, String...columns) throws SQLException;
    public abstract boolean deleteRow(String pkId) throws SQLException;
    public abstract void insertRow(String...columns) throws SQLException;
//    public abstract int insertRow(byte[] salt, String...columns) throws SQLException;
//    public abstract int signup(String...columns) throws SQLException;
    public abstract void updatePassword(String pkId, String...columns) throws SQLException;
    public abstract ObservableList activity(String accountNumber, String account) throws SQLException;
    public abstract void insertActivityRow(String...columns) throws SQLException;
    public abstract void insertRowTransaction(String...columns) throws SQLException;
    public abstract String[][] eTransferMessages(String accountNumber) throws SQLException;
    public abstract ObservableList eTransactions(String accountNumber) throws SQLException;
    public abstract ObservableList eTransactionsInTransactionsChecking(String accountNumber) throws SQLException;
    public abstract ObservableList eTransactionsInTransactionsSaving(String accountNumber) throws SQLException;
    public abstract boolean newMessages(String accountNumber) throws SQLException;
    public abstract void updateETransaction(String...columns) throws SQLException;
    public abstract void updateActivation(String accountNumber) throws SQLException;
    public abstract void activationCodeUpdate(String...columns) throws SQLException;
}
