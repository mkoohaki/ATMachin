package sample;

import java.sql.*;

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
}
