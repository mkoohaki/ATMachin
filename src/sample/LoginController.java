package sample.comtrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.AccountDatabase;
import sample.Partials;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;

public class LoginController {

    @FXML
    TextField accountNo;

    @FXML
    PasswordField password;

    @FXML
    javafx.scene.control.Button onClick, signupButton;

    private static final int PHONE_AND_ACCOUNT_DIGIT_NUMBER = 10;
    private static final String SHA2_256BIT_ALGORITHM = "SHA-256";

    @FXML
    private void login(ActionEvent event) throws SQLException {

        String accountNumber = accountNo.getText().trim();
        String pass = password.getText();

        try {
            if(Partials.isValidNumber(accountNumber)) {
                AccountDatabase db = new AccountDatabase();
                String[] accountInfo = db.login(accountNumber);

                if(accountInfo[0] != null) {

                    byte[] salt = accountInfo[1].getBytes(StandardCharsets.ISO_8859_1);
                    byte[] sha2Hash = createSHA2Hash(pass, salt);
                    String hashedPass = DatatypeConverter.printHexBinary(sha2Hash);

                    if (hashedPass.equals(accountInfo[2])) {

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("account.fxml"));
                        Parent root = loader.load();
                        AccountController accountController = loader.getController();
                        accountController.setInfo(accountInfo[0]);
                        onClick.getScene().setRoot(root);
                    } else {
                        Partials.alert("Account number and/or password is wrong", "error");
                    }
                } else {
                    Partials.alert("Account number and/or password is wrong", "error");
                }
            } else {
                Partials.alert("Account number is needed, it is a 10 digits number", "error");
                accountNumber = "";
                pass = "";
                accountNo.setText("");
                password.setText("");
            }
        } catch (Exception e) {
            System.err.println("Cannot load file (Login) !"+e);
        }
    }

    public void signup(ActionEvent event) {
        try {
//            Partials.windowOpen("Signup", "Royal Canadian Bank", 600, 400);
//            SignupController signupController = new SignupController();
//            signupController.setInfo();
//            Partials.windowClose(event);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent root = loader.load();
            SignupController signupController = loader.getController();
            signupController.setInfo();
            signupButton.getScene().setRoot(root);
        } catch (Exception e) {
            System.err.println("Cannot load file!");
        }
    }

    public void retrieving(ActionEvent event) {
        try {
            Partials.windowOpen("Retrieving", "Royal Canadian Bank", 600, 400);
            Partials.windowClose(event);
        } catch (Exception e) {
            System.err.println("Cannot load file!"+e);
        }
    }

//    public static boolean isValidNumber(String number) {
//        String allNumbers = "0123456789";
//        int numbers = 0;
//
//        if(number.length() == PHONE_AND_ACCOUNT_DIGIT_NUMBER) {
//            char[] ch = new char[number.length()];
//            for (int i = 0; i < number.length(); i++) {
//                ch[i] = number.charAt(i);
//                if (allNumbers.contains(Character.toString(ch[i])))
//                    numbers++;
//                else
//                    return false;
//            }
//            return numbers == PHONE_AND_ACCOUNT_DIGIT_NUMBER;
//        }
//        return false;
//    }

//    private void alert(String message, String kindOfAlert) {
//        try {
//            javafx.scene.control.Alert alert;
//            if(kindOfAlert.equals("error")) {
//                alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
//                alert.setTitle("Error");
//            } else {
//                alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
//                alert.setTitle("Successful signup");
//            }
//            alert.setContentText(message);
//            alert.showAndWait();
//        } catch (Exception e) {
//            System.err.println("Cannot load file!");
//        }
//    }

    public static byte[] createSHA2Hash(String inputValue, byte[] salt)
            throws Exception
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(salt);
        byteArrayOutputStream.write(inputValue.getBytes());
        byte[] saltedInputValue = byteArrayOutputStream.toByteArray();
        MessageDigest messageDigest = MessageDigest.getInstance(SHA2_256BIT_ALGORITHM);
        return messageDigest.digest(saltedInputValue);
    }
}
