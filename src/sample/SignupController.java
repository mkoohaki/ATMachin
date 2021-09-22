package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Locale;

public class SignupController {

    private static final int PHONE_AND_ACCOUNT_DIGIT_NUMBER = 10,
                             MINIMUM_DIGIT_NUMBER = 8,
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

    private Window window = new Window();
    String accountNumber, name, emailAddress, phoneNumber, pass, repass;

    @FXML
    private void signing(ActionEvent event) throws IOException {

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

            if(isValidNumber(accountNumber)) {
                if(!name.isEmpty()) {
                    if(isValidEmail(emailAddress)) {
                        if(isValidNumber(phoneNumber)){
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

                                    AccountDatabase db = new AccountDatabase();
                                    db.insertRow(accountNumber, salt, hashedPass, name, emailAddress, phoneNumber, "0", "0", "0");
                                    alert("Information saved", "notification");
                                    window.open("login", "Royal Canadian Bank", 600, 400);
                                    window.close(event);
                                } else {
                                    alert("Password and repassword are not matched", "error");
                                }
                            } else {
                                alert("Password must be between 8-16 character, at least:\n2 uppercase, 2 lowercase, 4 digits, and 1 special sign", "error");
                            }
                        } else {
                            alert("Phone number is not valid,\nIt is not mandatory", "error");
                        }
                    } else {
                        alert("Valid email address is needed", "error");
                    }
                } else {
                    alert("Full name is needed", "error");
                }
            } else {
                alert("Account number must be a 10 digit number", "error");
            }
        } catch (Exception e) {
            alert("Some information is wrong", "error");
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
            window.open("Login", "Royal Canadian Bank", 600, 400);
            window.close(event);
        } catch (Exception e) {
            System.err.println("Cannot load file!");
        }
    }

    @FXML
    private void visible(ActionEvent event) {
        try {
            String pass = password.getText();
            String repass = repassword.getText();
            String visiblePass = visiblePassword.getText();
            String visibleRepass = visibleRepassword.getText();
            if(password.isVisible()) {
                password.setVisible(false);
                repassword.setVisible(false);
                visiblePassword.setText(pass);
                visibleRepassword.setText(repass);
                visiblePassword.setVisible(true);
                visibleRepassword.setVisible(true);
            } else {
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

    private void alert(String message, String kindOfAlert) {
        try {
            Alert alert;
            if(kindOfAlert.equals("error")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
            } else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successful signup");
            }
            alert.setContentText(message);
            alert.showAndWait();
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

    public static boolean isValidNumber(String number) {
        String allNumbers = "0123456789";
        char[] ch = new char[number.length()];
        int numbers = 0;

        if(number.length() == PHONE_AND_ACCOUNT_DIGIT_NUMBER) {
            for (int i = 0; i < number.length(); i++) {
                ch[i] = number.charAt(i);
                if (allNumbers.contains(Character.toString(ch[i])))
                    numbers++;
                else
                    return false;
            }
            return numbers == PHONE_AND_ACCOUNT_DIGIT_NUMBER;
        }
        return false;
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
