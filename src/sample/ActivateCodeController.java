package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ActivateCodeController {

    @FXML
    javafx.scene.control.Button onClick;

    @FXML
    TextField activateCode;

    @FXML
    Text message;

    static String account;

    void setInfo(String accountNumber){

        account = accountNumber;
    }

    @FXML
    private void activate(ActionEvent event) throws Exception {

        message.setText("");
        AccountDatabase db = new AccountDatabase();
        String[] accountInfo = db.login(account);
        String activateNumber = activateCode.getText();

        if(accountInfo[10].equals(activateNumber)) {
            db.updateActivation(accountInfo[0]);
            SendEmail.mailing(accountInfo[4], accountInfo[3], "", "activation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("account.fxml"));
            Parent root = loader.load();
            AccountController accountController = loader.getController();
            accountController.setInfo(accountInfo[0]);
            onClick.getScene().setRoot(root);
        } else {
            activateNumber = "";
            activateCode.setText("");
            message.setText("Activation code is wrong!");
        }
    }

    @FXML
    private void close(ActionEvent event) {
        try {
            Partials.windowClose(event);
            Partials.windowOpen("login", "Royal Canadian Bank", 600, 400);
        } catch (Exception e) {
            System.err.println("Cannot load file!");
        }
    }
}
