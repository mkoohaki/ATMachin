package sample.interfaces;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sample.database.AccountDatabase;
import sample.Partials;
import sample.partials.SendEmail;

import javax.xml.bind.DatatypeConverter;
import java.sql.SQLException;
import java.util.Random;

public class RetrievingController {

    @FXML
    TextField accountNo, email, code;

    @FXML
    PasswordField password, repassword;

    @FXML
    Text textPass, textRepass, textCode;

    @FXML
    javafx.scene.control.Button getInfo, submitInfo;

    private static final int ACCOUNT_DIGIT_NUMBER = 10;
    String accountNumber, emailAddress, pass, repass;

    @FXML
    private void retrieving(ActionEvent event) throws SQLException {

        accountNumber = accountNo.getText();
        emailAddress = email.getText();

        try {
            if(Partials.isValidNumber(accountNumber)) {
                if(SignupController.isValidEmail(emailAddress)){

                    AccountDatabase db = new AccountDatabase();
                    String[] accountInfo = db.login(accountNumber);

                    if (accountInfo[0] != null) {
                        if (accountInfo[4].equals(emailAddress)) {
                            if (accountInfo[11].equals("True")) {

                                Random rnd = new Random();
                                int randomNumber = rnd.nextInt(999999);
                                String number = String.valueOf(randomNumber);

                                SendEmail.mailing(emailAddress, accountInfo[3], number, "activate code");
                                db.activationCodeUpdate(number, accountInfo[0]);

                                Partials.alert("Check your email for activation code","notification");

                                textPass.setVisible(true);
                                password.setVisible(true);
                                textRepass.setVisible(true);
                                repassword.setVisible(true);
                                code.setVisible(true);
                                textCode.setVisible(true);
                                submitInfo.setVisible(true);
                            } else {
                                Partials.alert("Account is not activated","error");
                            }
                        } else {
                            Partials.alert("Email address is wrong","error");
                        }
                    } else {
                        Partials.alert("Account number is wrong","error");
                    }
                } else {
                    Partials.alert("Enter a valid email address","error");
                }
            } else {
                Partials.alert("Account number is needed, it is a 10 digits number","error");
            }
        } catch (Exception e) {
            System.err.println("Cannot load file!"+e);
        }
    }

    @FXML
    private void submitNewPass(ActionEvent event) throws Exception {

        pass = password.getText();
        repass = repassword.getText();
        AccountDatabase db = new AccountDatabase();
        String[] accountInfo = db.login(accountNumber);

        if (code.getText().equals(accountInfo[10])) {
            if (SignupController.isValidPassword(pass)) {
                if (pass.equals(repass)) {
                    byte[] secureSalt = SignupController.createSecureRandomSalt();
                    byte[] sha2Hash = SignupController.createSHA2Hash(pass, secureSalt);
                    String salt = SignupController.hexToAscii(DatatypeConverter.printHexBinary(secureSalt));
                    String hashedPass = DatatypeConverter.printHexBinary(sha2Hash);

                    db.updatePassword(accountNumber, salt, hashedPass);
                    SendEmail.mailing(accountInfo[4], accountInfo[3], "", "password changed");
                    Partials.alert("Password is changed successfully", "notification");
                    Partials.windowOpen("login", "Royal Canadian Bank", 600, 400);
                    Partials.windowClose(event);
                } else {
                    Partials.alert("Password and repassword are not matched", "error");
                }
            } else {
                Partials.alert("Password must be between 8-16 character, at least:\n2 uppercase, 2 lowercase, 4 digits, and 1 special sign", "error");
            }
        } else {
            Partials.alert("Activation code is wrong", "error");
        }
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            Partials.windowOpen("Login", "Royal Canadian Bank", 600, 400);
            Partials.windowClose(event);
        } catch (Exception e) {
            System.err.println("Cannot load file!");
        }
    }
}
