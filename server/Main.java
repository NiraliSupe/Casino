package server;
import java.io.*;
import java.sql.SQLException;

public class Main {
    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException{
 // Display login page.
    	Login l = new Login();
    	l.setVisible(true);
    }
}
		