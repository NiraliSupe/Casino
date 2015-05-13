package server;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends JFrame{
	private JLabel l1, l2, l3, l4, l5, l6, l7, errorLabel;
	private JTextField email      	= new JTextField(20);
	private JPasswordField passWord   = new JPasswordField(20);
	private JTextField firstName  = new JTextField(20);
	private JTextField lastName   = new JTextField(20);
	private JPasswordField repassWord = new JPasswordField(20);
	private JTextField amount     = new JTextField(20);
	private JButton SignInButton  = new JButton("Sign In");
	private JButton backToSignIn  = new JButton("Sign In");
	private JButton SignUpButton  = new JButton("Sign UP");
	private JButton Submit        = new JButton("Submit");
	private JButton Submit_Amt    = new JButton("Submit");
	private ImagePanel LoginPanel = new ImagePanel(new ImageIcon(getClass().getResource("/resources/" +"bg18.jpg")).getImage());
	private JMenuBar bar          = new JMenuBar();
    private ServerSocket srvr = new ServerSocket(1246);
    
// Sets up the view and adds the components
	public Login() throws SQLException, ClassNotFoundException, IOException{
		DatabaseHandler.initialize();
		LoginPanel.setLayout(null);
		this.setSize(1300, 720);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
// close all connections on login page exit.		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				try {
					DatabaseHandler.closeSQLCon();
					srvr.close();
					dispose();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			    System.exit(0);
			  }
		});
		this.l3 = new JLabel("");
		this.l1 = new JLabel("Enter Email                    :");
		this.l2 = new JLabel("Enter Password          :");
		this.l6 = new JLabel("Re-enter Password    :");
		l4 = new JLabel("First Name                   :");
		l5 = new JLabel("Last Name                   :");
		l7 = new JLabel("Amount                      :");
		errorLabel = new JLabel("");
		l3.setBounds(100, 30, 400, 30);
		backToSignIn.setBounds(1100, 30, 100, 30);
		backToSignIn.setVisible(false);
		l1.setBounds(400, 110, 200, 30);
		l2.setBounds(400, 150, 200, 30);
		l1.setForeground(Color.orange);
		l2.setForeground(Color.orange);
		l4.setForeground(Color.orange);
		l5.setForeground(Color.orange);
		l7.setForeground(Color.orange);
		l6.setForeground(Color.orange);
		l7.setBounds(400, 190, 200, 30);
		l6.setBounds(400, 270, 200, 30);
		errorLabel.setBounds(620, 190, 300, 30);
		errorLabel.setForeground(Color.RED);
		email.setBounds(620, 110, 200, 30);
		passWord.setBounds(620, 150, 200, 30);
		SignInButton.setBounds(620, 220, 100, 30);
		SignUpButton.setBounds(720, 220, 100, 30);
		Submit_Amt.setBounds(620, 230, 200, 30);
		amount.setBounds(620, 190, 200, 30);
		Submit.setBounds(620, 340, 100, 30);
		bar.setVisible(false);
		Submit.setVisible(false);
		Submit_Amt.setVisible(false);
		amount.setVisible(false);
		l7.setVisible(false);
		LoginPanel.add(l3);
		LoginPanel.add(l1);
		LoginPanel.add(email);
		LoginPanel.add(l2);
		LoginPanel.add(passWord);
		LoginPanel.add(SignInButton);
		LoginPanel.add(SignUpButton);
    	LoginPanel.add(Submit);
		LoginPanel.add(bar);
		LoginPanel.add(l7);
		LoginPanel.add(errorLabel);
		LoginPanel.add(Submit_Amt);
		LoginPanel.add(amount);
		LoginPanel.add(backToSignIn);
		this.add(LoginPanel);
		SignInButton.addActionListener(new SignInButton());
		SignUpButton.addActionListener(new SignUpButton());
		Submit.addActionListener(new Submit());
		Submit_Amt.addActionListener(new Submit_Amt());
		backToSignIn.addActionListener(new backToSignIn());
	}
	
	public JTextField getEmail() {
		return email;
	}

	public void setEmail(JTextField email) {
		this.email = email;
	}

	public void connectServer() throws IOException{
		MultiThreadServer MTL = new MultiThreadServer(srvr,this);
		Thread ST = new Thread(MTL);
		ST.start();
	}

// First check if email and password is entered.
// Do email and password validations and direct user to main page.
// This method is for sign in of existing users.
	class SignInButton implements ActionListener{
		public void actionPerformed(ActionEvent e){
			try {
				connectServer();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			if(email.getText().isEmpty()&& passWord.getPassword().length < 1){
				errorLabel.setText("Please enter Email and Password");
				return;
			} else if(email.getText().isEmpty()){
				errorLabel.setText("Please enter Email");
				return;
			} else if(passWord.getPassword().length < 1){
				errorLabel.setText("Please enter Password");
				return;
			}
			if(!isEmailFormat(email.getText())){
				errorLabel.setText("Wrong Email address");
				return;
			}
			if(DatabaseHandler.getUserNameList().indexOf(email.getText()) >= 0){
				errorLabel.setText("Already Signed In");
	        	return;
	        }
			errorLabel.setText("");
			String status = DatabaseHandler.checkIn(String.valueOf(passWord.getPassword()), email.getText());
			if(!status.equals("200")){
				errorLabel.setText(status);
				return;
			}
			userLoginInfo ULI;
			try {
				ULI = new userLoginInfo();
				ULI.setVisible(true);
				email.setText("");
				passWord.setText("");
				Thread UG = new Thread(ULI);
				UG.start();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
// First check if all data is entered.
// Do email and password validations and sign up the new user.	
	class Submit implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(email.getText().isEmpty() || (passWord.getPassword().length < 1) ||
			   firstName.getText().isEmpty() || lastName.getText().isEmpty() ||
			   (repassWord.getPassword().length < 1)){
				errorLabel.setText("Please enter all Values");
				return;
			} else if(!(String.valueOf(passWord.getPassword())).equals(String.valueOf(repassWord.getPassword()))){
				errorLabel.setText("Password mismatch. Please re-enter password.");
				repassWord.setText("");
				passWord.setText("");
				return;
			} else if(!isEmailFormat(email.getText())){
				errorLabel.setText("Enter valid email address");
				return;
			}
			if(!validatePassword()){
				errorLabel.setText("Password must contain a minimum of 8 characters.");
				return;
			}
			try {
				connectServer();
			} catch (IOException e2) {
			
				e2.printStackTrace();
			}
			DatabaseHandler.createTable();
			String status = DatabaseHandler.registerUser(String.valueOf(passWord.getPassword()), 
										 email.getText(), 
										 firstName.getText(),
										 lastName.getText());
			if(!status.equals("200")){
				errorLabel.setText(status);
				return;
			}
			errorLabel.setText("");
			errorLabel.setBounds(620, 260, 300, 30);
			l1.setVisible(false);
	 		l2.setVisible(false);
	 		l3.setVisible(false);
	 		l4.setVisible(false);
	 		l5.setVisible(false);
	 		l6.setVisible(false);
	 		repassWord.setVisible(false);
	 		email.setVisible(false);
	 		passWord.setVisible(false);
	 		firstName.setVisible(false);
	 		lastName.setVisible(false);
	 		SignInButton.setVisible(false);
	 		SignUpButton.setVisible(false);
	 		Submit.setVisible(false); 
	 		Submit_Amt.setVisible(true);
	 		amount.setVisible(true);
	 		l7.setVisible(true);
		}
	}
	
// Allow new user to update balance.
	class Submit_Amt implements ActionListener{
		public void actionPerformed(ActionEvent e){
	    	if(!isNumeric(amount.getText())){
	    		errorLabel.setText("Please Enter Numeric value");
				amount.setText("");
				return;
			}
	    	try {
	    		String status = DatabaseHandler.updateAmount(amount.getText(), email.getText());
	    		if(!status.equals("200")){
	    			errorLabel.setText(status);
	    			return;
	    		}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			DatabaseHandler.setUserName(firstName.getText() + " " + lastName.getText());
			showSignInForm();
	 		userLoginInfo ULI;
			try {
				ULI = new userLoginInfo();
				ULI.setVisible(true);
				Thread UG = new Thread(ULI);
				UG.start();	
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			email.setText("");
		}
	}
// Redirect user to sign up form.	
	class SignUpButton implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			email.setText("");
			passWord.setText("");
			errorLabel.setText("");
			firstName.setBounds(620, 110, 200, 30);
			lastName.setBounds(620, 150, 200, 30);
			email.setBounds(620, 190, 200, 30);
			passWord.setBounds(620, 230, 200, 30);
			SignInButton.setVisible(false);
			SignUpButton.setVisible(false);
			repassWord.setBounds(620, 270, 200, 30);
			l4.setBounds(400, 110, 200, 30);
			l5.setBounds(400, 150, 200, 30);
			l1.setBounds(400, 190, 200, 30);
			l2.setBounds(400, 230, 200, 30);
			l6.setBounds(400, 270, 200, 30);
			errorLabel.setBounds(620, 310, 300, 30);
			l3.setBounds(100, 30, 400, 30);
			l1.setVisible(true);
	 		l2.setVisible(true);
	 		l4.setVisible(true);
	 		l5.setVisible(true);
	 		l6.setVisible(true);
	 		passWord.setText("");
	 		repassWord.setVisible(true);
	 		email.setVisible(true);
	 		passWord.setVisible(true);
	 		firstName.setVisible(true);
	 		lastName.setVisible(true);
			backToSignIn.setVisible(true);
			LoginPanel.add(l4);
			LoginPanel.add(l5);
			LoginPanel.add(l6);
			LoginPanel.add(firstName);
			LoginPanel.add(lastName);
			LoginPanel.add(repassWord);
			Submit.setVisible(true);
		}
	}
	
	class backToSignIn implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			showSignInForm();
			Submit.setVisible(false);
			backToSignIn.setVisible(false);
		}
	}
	
	public void showSignInForm(){
		l1.setBounds(400, 110, 200, 30);
		l2.setBounds(400, 150, 200, 30);
		l3.setBounds(100, 30, 400, 30);
		errorLabel.setBounds(620, 190, 300, 30);
  		email.setBounds(620, 110, 200, 30);
		passWord.setBounds(620, 150, 200, 30);
		SignInButton.setBounds(620, 220, 100, 30);
		SignUpButton.setBounds(720, 220, 100, 30);
 		l1.setVisible(true);
 		l2.setVisible(true);
 		l3.setVisible(true);
 		passWord.setText("");
 		l4.setVisible(false);
 		l5.setVisible(false);
 		l6.setVisible(false);
 		repassWord.setVisible(false);
 		email.setVisible(true);
 		passWord.setVisible(true);
 		firstName.setVisible(false);
 		lastName.setVisible(false);
 		Submit_Amt.setVisible(false);
 		amount.setVisible(false);
 		l7.setVisible(false);
 		errorLabel.setText("");
 		SignInButton.setVisible(true);
 		SignUpButton.setVisible(true);
	}
	
// Check if string contains integers.
	public boolean isNumeric(String s) {  
		return s.matches("[-+]?\\d*\\.?\\d+");  
	}
		
// Is email??
	public boolean isEmailFormat(String s) { 
		String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();  
	}
	
// Password must contain a minimum of 8 characters.		
	public boolean validatePassword(){
		if(passWord.getPassword().length  < 8){
			return false;
		}
		return true;
	}

}
