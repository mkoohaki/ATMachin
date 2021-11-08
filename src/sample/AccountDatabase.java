package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AccountDatabase extends Database {

    private final String accountTable = "account_info";
    private final String activityTable = "activity_info";
    private final String transactionTable = "transaction_info";
    private final String pkId = "accountNumber";
    private final int numColumns = 12;
    private final int maxRowsReturned = 9;
    Connection connection = getConnection();
    Statement statement = connection.createStatement();
    ObservableList<ModelTable> observableList = FXCollections.observableArrayList();
    ResultSet resultSet;

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
//        String sql = "SELECT * FROM" + table + "ORDER By " + pkId + "LIMIT " + maxRowsReturned;
        String sql = String.format("SELECT * FROM `%s` ORDER BY `%s` LIMIT %d", accountTable, pkId, maxRowsReturned);
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
        statement.executeQuery(sql);

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
//        String sql = "SELECT * FROM" + table + "ORDER By " + pkId + "LIMIT " + maxRowsReturned;
        String sql = String.format("SELECT * FROM `%s` ORDER BY `%s` LIMIT %d", accountTable, pkId, maxRowsReturned);

        while (resultSet.next()) {
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                data[i - 1] = resultSet.getMetaData().getColumnName(i);
            }
        }
        return data;
    }

    @Override
    public String[] login(String accountNumber) throws SQLException {

        String[] data = new String[12];
        //String sql = String.format("SELECT * FROM `%s` WHERE `accountNumber` = \"%s\" and `Password` = \"%s\"", table, accountNumber, password);
        String sql = String.format("SELECT * FROM `%s` WHERE `accountNumber` = \"%s\"", accountTable, accountNumber);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);

        if (resultSet.next()) {
            for (int i = 1; i < resultSet.getMetaData().getColumnCount()+1; i++) {
                data[i - 1] = resultSet.getObject(i).toString();
            }
        }
        return data;
    }

    @Override
    public String[] email(String email) throws SQLException {

        String[] data = new String[numColumns];
        String sql = String.format("SELECT * FROM `%s` WHERE `email` = \"%s\"", accountTable, email);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);

        if (resultSet.next()) {
            for (int i = 1; i < resultSet.getMetaData().getColumnCount(); i++) {
                data[i - 1] = resultSet.getObject(i).toString();
            }
        }
        return data;
    }

    @Override
    public boolean deleteRow(String pkId) throws SQLException {
        return false;
    }

    @Override
    public void insertRow(String... columns) throws SQLException {

        // insertRow("first", "second", "third");
        String sql = String.format("INSERT INTO `%s` (`accountNumber`, `salt`, `saltedPassword`, `fullName`, `email`, `phoneNumber`," +
                " `checkingBalance`,`savingBalance`, `lineBalance`, `activateNumber`, `activated`) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", accountTable);

        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, columns[0]);
        prepareStatement.setString(2, columns[1]);
        prepareStatement.setString(3, columns[2]);
        prepareStatement.setString(4, columns[3]);
        prepareStatement.setString(5, columns[4]);
        prepareStatement.setString(6, columns[5]);
        prepareStatement.setString(7, columns[6]);
        prepareStatement.setString(8, columns[7]);
        prepareStatement.setString(9, columns[8]);
        prepareStatement.setString(10, columns[9]);
        prepareStatement.setString(11, columns[10]);

        int numRows = prepareStatement.executeUpdate();

    }

    @Override
    public void updateRow(String pkId, String... columns) throws SQLException {

        String sql = String.format("UPDATE `%s` SET `checkingBalance` = ?, `savingBalance` = ?, `lineBalance` = ? WHERE `accountNumber` = ?", accountTable);
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, columns[0]);
        prepareStatement.setString(2, columns[1]);
        prepareStatement.setString(3, columns[2]);
        prepareStatement.setString(4, pkId);
        prepareStatement.executeUpdate();
    }

    @Override
    public void updatePassword(String pkId, String... columns) throws SQLException {

        String sql = String.format("UPDATE `%s` SET `salt` = ?, `saltedPassword` = ?, `activateNumber` = \"\" WHERE " +
                "`accountNumber` = ?", accountTable);
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, columns[0]);
        prepareStatement.setString(2, columns[1]);
        prepareStatement.setString(3, pkId);
        prepareStatement.executeUpdate();
    }

    @Override
    public ObservableList activity(String accountNumber, String account) throws SQLException {

        String sql = String.format("SELECT * FROM `%s` WHERE (`accountNo` = \"%s\" AND `accountTo` = \"%s\") OR " +
                "(`accountNo` = \"%s\" AND `accountFrom` = \"%s\")", activityTable, accountNumber, account, accountNumber, account);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);

        String status = null;

        while (resultSet.next()) {

            if (resultSet.getString("activityType").equals("Transfer")){
                if (resultSet.getString("accountFrom").equals(account))
                    status = "Transfer-Withdraw";
                else if (resultSet.getString("accountTo").equals(account))
                    status = "Transfer-Deposit";
            } else
                status = resultSet.getString("activityType");


            observableList.add(new ModelTable(
                    status, resultSet.getString("amount"), resultSet.getString("date")));
        }
        return observableList;
    }

    @Override
    public void insertActivityRow(String... columns) throws SQLException {

        String sql = String.format("INSERT INTO `%s` (`accountNo`, `activityType`, `accountFrom`, `accountTo`, `amount`) " +
                "VALUE (?, ?, ?, ?, ?)", activityTable);

        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, columns[0]);
        prepareStatement.setString(2, columns[1]);
        prepareStatement.setString(3, columns[2]);
        prepareStatement.setString(4, columns[3]);
        prepareStatement.setString(5, columns[4]);

        int numRows = prepareStatement.executeUpdate();

    }

    @Override
    public void insertRowTransaction(String... columns) throws SQLException {

        String sql = String.format("INSERT INTO `%s` (`confirm`, `account_from`, `account_to`, `amount`) " +
                "VALUE (?, ?, ?, ?)", transactionTable);

        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, columns[0]);
        prepareStatement.setString(2, columns[1]);
        prepareStatement.setString(3, columns[2]);
        prepareStatement.setString(4, columns[3]);

        int numRows = prepareStatement.executeUpdate();

    }
//
//    @Override
//    public String[][] eTransferMessages(String accountNumber) throws SQLException {
//
//        String[][] data = new String[maxRowsReturned][6];
//        String sql = String.format("SELECT * FROM `%s` WHERE (`account_to` = \"%s\" AND `confirm` = '%b')",
//                transactionTable, accountNumber, false);
//        statement = connection.createStatement();
//
//        ResultSet resultSet = statement.executeQuery(sql);
//
//        int row = 0;
//        while (resultSet.next()) {
//            row++;
//            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
//                data[row - 1][i - 1] = resultSet.getObject(i).toString();
//
//            }
//        }
//        return data;
//    }

    @Override
    public String[][] eTransferMessages(String accountNumber) throws SQLException {

        String[][] data = new String[maxRowsReturned][6];
        String sql = String.format("SELECT * FROM `%s` WHERE (`account_to` = \"%s\")",
                transactionTable, accountNumber, false);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);

        int row = 0;
        while (resultSet.next()) {
            row++;
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                data[row - 1][i - 1] = resultSet.getObject(i).toString();

            }
        }
        return data;
    }

    @Override
    public ObservableList eTransactions(String accountNumber) throws SQLException {

        String sql = String.format("SELECT * FROM `%s` WHERE (`account_to` = \"%s\" OR `account_from` = \"%s\")",
                transactionTable, accountNumber, accountNumber);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            String status = null;
            if (resultSet.getString("confirm").equals("Not Confirmed")) {
                if (resultSet.getString("account_from").equals(accountNumber))
                    status = "cancel";
                else if (resultSet.getString("account_to").equals(accountNumber))
                    status = "confirm";
            } else if (resultSet.getString("confirm").equals("Confirmed"))
                status = "confirmed";
            else if (resultSet.getString("confirm").equals("Canceled"))
                status = "canceled";

            observableList.add(new ModelTable(
                    resultSet.getString("account_from"), resultSet.getString("account_to"),
                    resultSet.getString("amount"), resultSet.getString("date"), status));
        }
        return observableList;
    }

    @Override
    public ObservableList eTransactionsInTransactionsChecking(String accountNumber) throws SQLException {

//        String sql = String.format("SELECT * FROM `%s` WHERE (`account_to` = \"%s\" AND `confirm` = \"Confirmed\" AND " +
//                            " `depositedAccount` = \"%s\" ) OR (`account_from` = \"%s\" AND `confirm` <> \"Canceled\")",
//                    transactionTable, accountNumber, account, accountNumber);

        String sql = String.format("SELECT * FROM `%s` WHERE (`account_to` = \"%s\" AND `confirm` = \"Confirmed\" AND " +
                        " `depositedAccount` = \"Checking\" ) OR (`account_from` = \"%s\" AND `confirm` <> \"Canceled\")",
                        transactionTable, accountNumber,  accountNumber);

        statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);
        String status = null;

        while (resultSet.next()) {

            if (resultSet.getString("account_from").equals(accountNumber)) {
                status = "ETransfer-Withdraw";
            } else if (resultSet.getString("account_to").equals(accountNumber))
                status = "ETransfer-Deposit";

            observableList.add(new ModelTable(
                    status, resultSet.getString("amount"), resultSet.getString("date")));
        }
        return observableList;
    }

    @Override
    public ObservableList eTransactionsInTransactionsSaving(String accountNumber) throws SQLException {

        String sql = String.format("SELECT * FROM `%s` WHERE (`account_to` = \"%s\" AND `confirm` = \"Confirmed\"  AND " +
                        " `depositedAccount` = \"Saving\" )", transactionTable, accountNumber);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {

            observableList.add(new ModelTable(
                    "ETransfer-Deposit", resultSet.getString("amount"), resultSet.getString("date")));
        }
        return observableList;
    }

    @Override
    public boolean newMessages(String accountNumber) throws SQLException {

        String sql = String.format("SELECT * FROM `%s` WHERE (`account_to` = \"%s\" AND `confirm` = \"Not Confirmed\")",
                transactionTable, accountNumber);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            observableList.add(new ModelTable(
                    resultSet.getString("confirm"), resultSet.getString("account_from"),
                    resultSet.getString("account_to"), resultSet.getString("amount"),
                    resultSet.getString("date")));
        }
        return !observableList.isEmpty();
    }

    @Override
    public void updateETransaction(String... columns) throws SQLException {

        String sql = String.format("UPDATE `%s` SET `confirm` = ?, `depositedAccount` = ? WHERE `account_from` = ? AND " +
                "`account_to` = ? AND `amount` = ? AND `date` = ?", transactionTable);
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, columns[0]);
        prepareStatement.setString(2, columns[1]);
        prepareStatement.setString(3, columns[2]);
        prepareStatement.setString(4, columns[3]);
        prepareStatement.setString(5, columns[4]);
        prepareStatement.setString(6, columns[5]);

        prepareStatement.executeUpdate();
    }

    @Override
    public void updateActivation(String pkId) throws SQLException {

        String sql = String.format("UPDATE `%s` SET `activateNumber` = \"\", `activated` = \"True\" WHERE `accountNumber` = ?", accountTable);
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, pkId);
        prepareStatement.executeUpdate();
    }

    @Override
    public void activationCodeUpdate(String... columns) throws SQLException {

        String sql = String.format("UPDATE `%s` SET `activateNumber` = ? WHERE `accountNumber` = ?", accountTable);
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setString(1, columns[0]);
        prepareStatement.setString(2, columns[1]);
        prepareStatement.executeUpdate();
    }
}
