package sample;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class DBTest {


    public static void Example1() throws SQLException {
        AccountDatabase db = new AccountDatabase();
        String[][] rows = db.getAllRows();
        String[] columnName = db.getAllColumns();

        System.out.println();
        int row = 1;
        for (String[] columns : rows) {
            System.out.println("Client # " + row);
            row++;
            for (int i = 0; i < columnName.length; i++) {
                if (columns[i] == null)
                    break;
                try {
                    if (i>5) {
                        Currency currency = new Currency();
                        System.out.printf("\t%s : %s%n", columnName[i], currency.currency("Cad", columns[i]));
                    } else{
                        System.out.printf("\t%s : %s%n", columnName[i], columns[i]);
                    }
                } catch (Exception e) {
                    System.out.println("Error: "+ e);
                }
            }
            System.out.println("------------------------------------------------------");
        }
    }

    public static void main(String[] args) {

    /*
        try {
            Database db = new Database();
            System.out.println("Success!");
        }
        catch (java.sql.SQLException e) {
            System.out.println("Connection failed! " + e);
        }
    }
     */



        try {
            AccountDatabase db = new AccountDatabase();
            //int numRows = db.insertRow("5647896321", "64656465", "M cooper", "mc@gmail.com", "4163334567", "100.69", "200.79", "300.89");
            //System.out.println("Num rows = " + numRows);
            System.out.println("Success!");
        }
        catch (java.sql.SQLException e) {
            System.out.println("Connection failed! " + e);
        }
    }
}
