package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.sql.SQLException;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javax.xml.bind.DatatypeConverter;

public class RetrievingController {

    @FXML
    TextField accountNo, email;

    @FXML
    PasswordField password, repassword;

    @FXML
    Text textPass, textRepass;

    @FXML
    javafx.scene.control.Button getInfo, submitInfo;

    private static final int ACCOUNT_DIGIT_NUMBER = 10;
    private Window window = new Window();
    String accountNumber, emailAddress, pass, repass;


    @FXML
    private void retrieving(ActionEvent event) throws SQLException {

        accountNumber = accountNo.getText();
        emailAddress = email.getText();

        try {
            if(SignupController.isValidNumber(accountNumber)) {
                if(SignupController.isValidEmail(emailAddress)){

                    AccountDatabase db = new AccountDatabase();
                    Object[] accountInfo = db.login(accountNumber);

                    if (accountInfo[0] != null) {
                        String gotEmail = accountInfo[4].toString();

                        if (gotEmail.equals(emailAddress)) {
//                            FXMLLoader loader = new FXMLLoader(getClass().getResource("account.fxml"));
//                            Parent root = loader.load();
//                            AccountController accountController = loader.getController();
//                            accountController.setInfo(accountInfo[0], accountInfo[6], accountInfo[7], accountInfo[8]);
//                            onClick.getScene().setRoot(root);
                            textPass.setVisible(true);
                            password.setVisible(true);
                            textRepass.setVisible(true);
                            repassword.setVisible(true);
                            submitInfo.setVisible(true);
                        } else {
                            alert("Email address is wrong","error");
                        }
                    } else {
                        alert("Account number is wrong","error");
                    }
                } else {
                    alert("Enter a valid email address","error");
                }
            } else {
                alert("Account number is needed, it is a 10 digits number","error");
            }
        } catch (Exception e) {
            System.err.println("Cannot load file!"+e);
        }
    }

    private void alert(String message, String kindOfAlert) {
        try {
            javafx.scene.control.Alert alert;
            if(kindOfAlert.equals("error")) {
                alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Error");
            } else {
                alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Successful signup");
            }
            alert.setContentText(message);
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Cannot load file!");
        }
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            window.open("Login", "Royal Canadian Bank", 600, 400);
            window.close(event);
        } catch (Exception e) {
            System.err.println("Cannot load file!");
        }
    }

    @FXML
    private void submitNewPass(ActionEvent event) throws Exception {

        pass = password.getText();
        repass = repassword.getText();

        if (SignupController.isValidPassword(pass)) {
            if (pass.equals(repass)) {

                byte[] secureSalt = SignupController.createSecureRandomSalt();
                byte[] sha2Hash = SignupController.createSHA2Hash(pass, secureSalt);
                String salt = SignupController.hexToAscii(DatatypeConverter.printHexBinary(secureSalt));
                String hashedPass = DatatypeConverter.printHexBinary(sha2Hash);

                AccountDatabase db = new AccountDatabase();
                db.updatePassword(accountNumber, salt, hashedPass);

                alert("Password is changed successfully", "notification");
                window.open("login", "Royal Canadian Bank", 600, 400);
                window.close(event);
            } else {
                alert("Password and repassword are not matched", "error");
            }
        } else {
            alert("Password must be between 8-16 character, at least:\n2 uppercase, 2 lowercase, 4 digits, and 1 special sign", "error");
        }
    }
}
