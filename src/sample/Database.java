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
    public abstract boolean updateRow(String pkId, String...columns) throws SQLException;
    public abstract boolean deleteRow(String pkId) throws SQLException;
    public abstract int insertRow(String...columns) throws SQLException;
//    public abstract int insertRow(byte[] salt, String...columns) throws SQLException;
//    public abstract int signup(String...columns) throws SQLException;
    public abstract boolean updatePassword(String pkId, String...columns) throws SQLException;
    public abstract ObservableList activity(String accountNumber, String account) throws SQLException;
    public abstract int insertActivityRow(String...columns) throws SQLException;
    public abstract int insertRowTransaction(String...columns) throws SQLException;
    public abstract String[][] eTransferMessages(String accountNumber) throws SQLException;
    public abstract ObservableList eTransactions(String accountNumber) throws SQLException;
    public abstract ObservableList eTransactionsInTransactions(String accountNumber, String account) throws SQLException;
    public abstract boolean newMessages(String accountNumber) throws SQLException;
    public abstract boolean updateETransaction(String...columns) throws SQLException;
}
