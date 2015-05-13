Login Page:
1. Allows new user to register.
2. It also allow user to give initial balance. 
3. Allows existing user to sign with their username(it should be an email) and password (must contain at least 8 characters)

Menu Page:
Contains menu bar which has following ietms.
1. Signout.
2. Game Lisitng. - BlackJack, Poker_TexasHoldem, Slotmachine
3. User account - See current balace and update balance.

Each game start only after user place a bet. Bet should be multiple of 10.
User can play all three game at the same time but only per time.

Database: Sqlite databse has used store the user information.

External lib needed: sqlite-jdbc-3.8.7.jar .

Compile:

Compile java files in the following order:

javac -cp "lib/sqlite-jdbc-3.8.7.jar;." server/DatabaseHandler.java

javac -cp "server/DatabaseHandler;." blackjack/*.java

javac -cp "server/DatabaseHandler;." slotmachine/*.java

javac -cp "server/DatabaseHandler;." poker_TexasHoldem/*.java

javac -cp "lib/sqlite-jdbc-3.8.7.jar;." server/*.java

Run:

java -cp "lib/sqlite-jdbc-3.8.7.jar;." server.Main
