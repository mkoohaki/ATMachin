# ATMachin

This is a JavaFx application.<br>
• It works like an ATM machine/Bank account application.<br>
•	It encrypts the password before saving password on the local database.<br>
•	It uses XAMPP as a local server (PHPMyAdmin) and is connected to MySql as a local database. <br>
•	For encryption, it salts the password with a 10 random characters and using SHA2-256 for hashing. Just the result save in database and nobody has access to the password.
•	It uses SQL commands to get, insert, update, and delete data.<br>

• At the begining, user faces up with Login page.
<img src="images/LogIn.JPG" width="250" height="auto"><br>

• Users can signup if they have not registered yet.
<img src="images/SignUp.JPG" width="250" height="auto"><br>

• In case the password is forgotten, users can create another password.<br>
<img src="images/Forgot.JPG" width="250" height="auto"><img src="images/Forgot1.JPG" width="250" height="auto"><br>

• When user login successfully, there will be appeared a red attention message if a not confirmed etransaction exists.<br>
<img src="images/Account.JPG" width="250" height="auto"><img src="images/New%20etransaction.JPG" width="250" height="auto"><br>

• If users click on the E-Transaction history/New e-transaction buttons, they can see the history of etransactions, cancel the amount sent (before than other uuser confirmation, or confirm the transaction. If user confirms transaction, a new window pops up and user has to select the deposit account.<br>
<img src="images/E-Transactions.JPG" width="250" height="auto"><img src="images/EtransferDeposit.JPG" width="250" height="auto"><br>

• User can change the unit currency with select currensy in the dropdown menu, application will get the exchange rateautomatically with using rest API from ExchangeRate-API.<br>
<img src="images/Account-1.jpg" width="250" height="auto"><img src="images/Account-2.JPG" width="250" height="auto"><br>

• User can transfer, withdraw, deposit, or Etransfer money.<br>
<img src="images/Transfer.JPG" width="250" height="auto"><img src="images/WthDep.JPG" width="250" height="auto"><img src="images/Etransfer.JPG" width="250" height="auto"><br>

• User can see the transactions in each account with clicking on the amount.<br>
<img src="images/Chequing%20transactions.JPG" width="250" height="auto"><img src="images/Saving%20transactions.JPG" width="250" height="auto"><br>

• You can see the project files.
<img src="images/projectTree.JPG" width="250" height="auto"><img src="images/Saving%20transactions.JPG" width="250" height="auto"><br>
