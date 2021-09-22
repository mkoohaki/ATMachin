package sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class AccountDatabase extends Database {

    private String table = "account_info";
    private String pkId = "AccountNumber";
    private final int numColumns = 9;
    private final int loginColumns = 9;
    private int maxRowsReturned = 9;
    Connection connection = getConnection();
    Statement statement = connection.createStatement();

    public AccountDatabase() throws SQLException {

        super();
    }

    @Override
    public String[] getRow(String pkId) throws SQLException {
        return new String[0];
    }

    @Override
    public String[][] getAllRows() throws SQLException {

        String[][] data = new String[maxRowsReturned][numColumns];
        connection = getConnection();
//        String sql = "SELECT * FROM" + table + "ORDER By " + pkId + "LIMIT " + maxRowsReturned;
        String sql = String.format("SELECT * FROM `%s` ORDER BY `%s` LIMIT %d", table, pkId, maxRowsReturned);
        statement = connection.createStatement();


        /*
          3 types of executions
            execute
                any statement
            executeQuery
                selection statements
            executeUpdate
                data altering statement
         */
        ResultSet resultSet = statement.executeQuery(sql);

        int row = 0;
        while (resultSet.next()) {
            row++;
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                /*
                System.out.printf("Row = %d, Column name = `%s`, Column value = `%s`%n", row, resultSet.getMetaData().getColumnName(i),
                        resultSet.getObject(i).toString());

                 */
                data[row - 1][i - 1] = resultSet.getObject(i).toString();

            }
        }
        return data;
    }
    //SOUT
    //PSVM


    @Override
    public String[] getAllColumns() throws SQLException {
        String[] data = new String[numColumns];
        connection = getConnection();
//        String sql = "SELECT * FROM" + table + "ORDER By " + pkId + "LIMIT " + maxRowsReturned;
        String sql = String.format("SELECT * FROM `%s` ORDER BY `%s` LIMIT %d", table, pkId, maxRowsReturned);
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                data[i - 1] = resultSet.getMetaData().getColumnName(i);
            }
        }
        return data;
    }

    @Override
    public String[] login(String accountNumber) throws SQLException {
        String[] data = new String[loginColumns];
        connection = getConnection();
        //String sql = String.format("SELECT * FROM `%s` WHERE `AccountNumber` = \"%s\" and `Password` = \"%s\"", table, accountNumber, password);
        String sql = String.format("SELECT * FROM `%s` WHERE `AccountNumber` = \"%s\"", table, accountNumber);

        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        if (resultSet.next()) {
            for (int i = 1; i < resultSet.getMetaData().getColumnCount(); i++) {
                data[i - 1] = resultSet.getObject(i).toString();
                System.out.println(data[i - 1]);
            }
        }
        return data;
    }
//
//    @Override
//    public byte[] loginSalt(String accountNumber) throws SQLException {
////        byte[] salt = new byte[16];
////        connection = getConnection();
////        String sql = String.format("SELECT `Salt` FROM `%s` WHERE `AccountNumber` = `%s`", table, accountNumber);
////
////        statement = connection.createStatement();
////        ResultSet resultSet = statement.executeQuery(sql);
//////
//////        if(resultSet.next()){
//////            for (int i = 1; i < resultSet.getMetaData().getColumnCount(); i++) {
//////                data[i - 1] = resultSet.getObject(i).toString();
//////                System.out.println(data[i - 1]);
//////            }
//////        }
////        salt = statement.executeQuery(sql);
////        return resultSet;
//
//        byte[] salt = new byte[16];
//        connection = getConnection();
//        String sql = String.format("SELECT `Salt` FROM `%s` WHERE `AccountNumber` = `%s`", table, accountNumber);
//
//        statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(sql);
//
//        File image = new File(salt);
//        try (FileOutputStream fos = new FileOutputStream(salt)) {
//            byte[] buffer = new byte[1024];
//
//            // Get the binary stream of our BLOB data
//            InputStream is = resultSet.getBinaryStream("Salt");
//            while (is.read(buffer) > 0) {
//                fos.write(buffer);
//            }
//            data[2] = fos;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public boolean deleteRow(String pkId) throws SQLException {
        return false;
    }

    @Override
    public int insertRow(String... columns) throws SQLException {

        // insertRow("first", "second", "third");
        String sql = String.format("INSERT INTO `%s` (`AccountNumber`, `Salt`, `SaltedPassword`, `FullName`, `Email`, `PhoneNumber`," +
                " `CheckingBalance`,`SavingBalance`, `LineBalance`) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?)", table);

        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, columns[0]);
        //prepareStatement.setString(2, salt);
        prepareStatement.setString(2, columns[1]);
        prepareStatement.setString(3, columns[2]);
        prepareStatement.setString(4, columns[3]);
        prepareStatement.setString(5, columns[4]);
        prepareStatement.setString(6, columns[5]);
        prepareStatement.setString(7, columns[6]);
        prepareStatement.setString(8, columns[7]);
        prepareStatement.setString(9, columns[8]);

        int numRows = prepareStatement.executeUpdate();

        return numRows;
    }

    @Override
    public boolean updateRow(String pkId, String... columns) throws SQLException {

        String sql = String.format("UPDATE `%s` SET `CheckingBalance` = ?, `SavingBalance` = ?, `LineBalance` = ? WHERE `AccountNumber` = ?", table);
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, columns[0]);
        prepareStatement.setString(2, columns[1]);
        prepareStatement.setString(3, columns[2]);
        prepareStatement.setString(4, pkId);
        prepareStatement.executeUpdate();
        return true;
    }

    @Override
    public boolean updatePassword(String pkId, String... columns) throws SQLException {

        String sql = String.format("UPDATE `%s` SET `Salt` = ?, `SaltedPassword` = ? WHERE `AccountNumber` = ?", table);
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, columns[0]);
        prepareStatement.setString(2, columns[1]);
        prepareStatement.setString(3, pkId);
        prepareStatement.executeUpdate();
        return true;
    }
}
