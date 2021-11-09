package sample.interfaces;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.validator.routines.EmailValidator;
import sample.Partials;
import sample.database.AccountDatabase;
import sample.partials.SendEmail;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SignupController  implements Initializable {

    private static final int MINIMUM_DIGIT_NUMBER = 8,
                             MAXIMUM_DIGIT_NUMBER = 16,
                             MINIMUM_NUMBERS = 4,
                             MINIMUM_LOWERCASE_NUMBER = 2,
                             MINIMUM_UPPERCASE_NUMBER = 2,
                             MINIMUM_SPECIAL_CHARACTER_NUMBER = 1;

    private static final String SHA2_256BIT_ALGORITHM = "SHA-256";
    private static final byte SALT_LENGTH = 16;;

    @FXML
    TextField accountNo, fullName, email, phoneNo;

    @FXML
    PasswordField password, repassword;

    @FXML
    TextField visiblePassword, visibleRepassword;

    @FXML
    Button eye;

    String accountNumber, name, emailAddress, phoneNumber, pass, repass;

    void setInfo() throws SQLException, IOException {

        eye.setStyle("-fx-background-image: url('sample/images/eye.png'); " +
                     "-fx-background-repeat: no-repeat; " +
                     "-fx-background-size: 100% 100%;");
    }

    @FXML
    private void signing(ActionEvent event) {

    try {
            accountNumber = accountNo.getText();
            name = fullName.getText();
            emailAddress = email.getText();
            phoneNumber = phoneNo.getText();

            if(password.isVisible()) {
                pass = password.getText();
                repass = repassword.getText();
            }
            else {
                pass = visiblePassword.getText();
                repass = visibleRepassword.getText();
            }

            if(Partials.isValidNumber(accountNumber)) {

                if(!Partials.isValidAccount(accountNumber)) {
                    if(!name.isEmpty()) {
                        if(isValidEmail(emailAddress)) {
                            if(isUniqueEmail(emailAddress)) {
                                if (Partials.isValidNumber(phoneNumber)) {
                                    if (isValidPassword(pass)) {
                                        if (pass.equals(repass)) {

                                            byte[] secureSalt = createSecureRandomSalt();
                                            System.out.println("Secure Salt = " + DatatypeConverter.printHexBinary(secureSalt));

                                            byte[] sha2Hash = createSHA2Hash(pass, secureSalt);
                                            System.out.println("Password: " + pass);
                                            String salt = hexToAscii(DatatypeConverter.printHexBinary(secureSalt));
                                            System.out.println("Generated salt in ASCII: " + salt);
                                            System.out.println("Salted password in ASCII: " + salt + pass);
                                            String hashedPass = DatatypeConverter.printHexBinary(sha2Hash);
                                            System.out.println("Generated hash: " + hashedPass);

                                            String activationCode = Partials.activationCode();

                                            AccountDatabase db = new AccountDatabase();
                                            db.insertRow(accountNumber, salt, hashedPass, name, emailAddress, phoneNumber, "0", "0", "0", activationCode, "False");
                                            Partials.windowOpen("login", "Royal Canadian Bank", 600, 400);
                                            Partials.windowClose(event);

                                            SendEmail.mailing(emailAddress, name, activationCode, "", "", "signed up");
                                            Partials.alert("You signed up successfully, for account activation\n" +
                                                                   "please check your email address for activation code.",
                                                         "notification");
                                        } else {
                                            Partials.alert("Password and repassword are not matched", "error");
                                        }
                                    } else {
                                        Partials.alert("Password must be between 8-16 character, at least:\n2 uppercase, 2 lowercase, 4 digits, and 1 special sign", "error");
                                    }
                                } else {
                                    Partials.alert("Phone number is not valid,\nIt is not mandatory", "error");
                                }
                            } else {
                                Partials.alert("Email address is already used", "error");
                            }
                        } else {
                            Partials.alert("Valid email address is needed", "error");
                        }
                    } else {
                        Partials.alert("Full name is needed", "error");
                    }
                } else {
                    Partials.alert("Account number is already registered", "error");
                }
            } else {
                Partials.alert("Account number must be a 10 digit number", "error");
            }
        } catch (Exception e) {
            Partials.alert("Some information is wrong", "error");
            System.err.println("Cannot load file! "+e);
        }
    }

    @FXML
    private void clear (ActionEvent event) {
        accountNo.setText("");
        fullName.setText("");
        phoneNo.setText("");
        email.setText("");
        password.setText("");
        repassword.setText("");
        visiblePassword.setText("");
        visibleRepassword.setText("");
        password.setVisible(true);
        repassword.setVisible(true);
        visiblePassword.setVisible(false);
        visibleRepassword.setVisible(false);
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }

    @FXML
    private void visible(ActionEvent event) {
        try {
            String pass = password.getText();
            String repass = repassword.getText();
            String visiblePass = visiblePassword.getText();
            String visibleRepass = visibleRepassword.getText();
            if(password.isVisible()) {

                eye.setStyle("-fx-background-image: url('sample/images/closedEye.png'); " +
                        "-fx-background-repeat: no-repeat; " +
                        "-fx-background-size: 100% 100%;");
                password.setVisible(false);
                repassword.setVisible(false);
                visiblePassword.setText(pass);
                visibleRepassword.setText(repass);
                visiblePassword.setVisible(true);
                visibleRepassword.setVisible(true);
            } else {

                eye.setStyle("-fx-background-image: url('sample/images/eye.png'); " +
                        "-fx-background-repeat: no-repeat; " +
                        "-fx-background-size: 100% 100%;");
                password.setText(visiblePass);
                repassword.setText(visibleRepass);
                password.setVisible(true);
                repassword.setVisible(true);
                visiblePassword.setVisible(false);
                visibleRepassword.setVisible(false);
            }
        } catch (Exception e) {
            System.err.println("Cannot load file!");
        }
    }

    public static boolean isValidEmail(String email) {
        // create the EmailValidator instance
        EmailValidator validator = EmailValidator.getInstance();

        // check for valid email addresses using isValid method
        return validator.isValid(email);
    }

    public static boolean isUniqueEmail(String email) throws SQLException {

        AccountDatabase db = new AccountDatabase();
        String[] accountInfo = db.email(email);

        return accountInfo[4] == null;
    }

    public static boolean isValidPassword(String password) {

        String allLowercase = "abcdefghijklmnopqrstuvwxyz";
        String allNumbers = "0123456789";
        String specialCharacters = "!/.,\";:(){}[]@#$%^&*-_=+<>";

        char[] ch = new char[password.length()];
        int lowercaseNumber = 0;
        int uppercaseNumber = 0;
        int specialCharacterNumber = 0;
        int numbers = 0;

        if(password.length() >= MINIMUM_DIGIT_NUMBER && password.length() <= MAXIMUM_DIGIT_NUMBER) {
            for (int i = 0; i < password.length(); i++) {
                ch[i] = password.charAt(i);
                if (allLowercase.contains(Character.toString(ch[i]))) {
                    lowercaseNumber++;
                } else if (allLowercase.toUpperCase(Locale.ROOT).contains(Character.toString(ch[i]))) {
                    uppercaseNumber++;
                } else if (allNumbers.contains(Character.toString(ch[i]))) {
                    numbers++;
                } else if (specialCharacters.contains(Character.toString(ch[i]))) {
                    specialCharacterNumber++;
                }
            }
            return lowercaseNumber >= MINIMUM_LOWERCASE_NUMBER && uppercaseNumber >= MINIMUM_UPPERCASE_NUMBER &&
                    specialCharacterNumber >= MINIMUM_SPECIAL_CHARACTER_NUMBER && numbers >= MINIMUM_NUMBERS;
        } else {
            return false;
        }
    }

    public static byte[] createSecureRandomSalt() {
        byte[] secureSalt = new byte[SALT_LENGTH];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secureSalt);
        return secureSalt;
    }

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

    static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

}
