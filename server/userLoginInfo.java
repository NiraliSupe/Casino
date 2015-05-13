package server;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import javax.swing.*;

class userLoginInfo extends JFrame implements Runnable {
	private JLabel l1, l2, errorLabel;
	private JTextField availableAmt = new JTextField(20);
	private JTextField updateAmt    = new JTextField(20);
	private ImagePanel MenuPanel    = new ImagePanel(new ImageIcon(getClass().getResource("/resources/" +"bg8.jpg")).getImage());
	private JMenuBar bar            = new JMenuBar();
	private JButton updateButton    = new JButton("Update");
	private JMenuItem signOut;
	private int availableBalance;
	private String email;
	private Socket skt = null;
    private BufferedReader in;
    private boolean firstTime = true;
    private userGameInfo userGameInfo;
    
	public userLoginInfo() throws ClassNotFoundException, UnknownHostException, IOException{
		if(firstTime){
			firstTime = false;
			this.skt = new Socket("localhost", 1246);
		}
		this.availableBalance = DatabaseHandler.getAvailableAmt();
		MenuPanel.setLayout(null);
		l1 = new JLabel("Current Balance                   :");
		l2 = new JLabel("Enter Amount                      :");
		l1.setForeground(Color.orange);
		l2.setForeground(Color.orange);
		errorLabel = new JLabel("");
		l1.setBounds(400, 70, 200, 30);
		l2.setBounds(400, 110, 200, 30);
		availableAmt.setBounds(620, 70, 150, 30);
		updateAmt.setBounds(620, 110, 150, 30);
		updateButton.setBounds(790, 110, 100, 30);
		errorLabel.setBounds(620, 140, 150, 30);
		errorLabel.setForeground(Color.red);
		l1.setVisible(false);
		l2.setVisible(false);
		availableAmt.setVisible(false);
		updateAmt.setVisible(false);
		updateButton.setVisible(false);
	    JMenu addMenu         = new JMenu( "Games" );
	    JMenu addAcc          = new JMenu( "Your Account" );
	    JMenu addHelp         = new JMenu( "Help" );
	    signOut    = new JMenuItem( "Sign out" );
	    JMenuItem  updateBalance  = new JMenuItem( "Update Your Balance" );
	    JMenuItem  viewUrBalance  = new JMenuItem( "View Your Balance" );
	    JMenuItem blackJack   = new JMenuItem( "BlackJack" );
	    JMenuItem slotMachine = new JMenuItem( "Slot Machine" );
	    JMenuItem poker       = new JMenuItem( "Poker" );
	    JMenu header         = new JMenu( "Hello, "+ DatabaseHandler.getUserName() + "      " );
	    header.add( signOut );
	    addAcc.add( viewUrBalance );
	    addAcc.add( updateBalance );
	    addMenu.add( blackJack ); 
	    addMenu.add( slotMachine );
	    addMenu.add( poker );
	    bar.add( header );
	    bar.add( addAcc );
	    bar.add( addMenu );
	    bar.add( addHelp );
	    setJMenuBar( bar );  
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				DatabaseHandler.getUserNameList().remove(email);
				setVisible(false);
				dispose();
			  }
		});
		this.setSize(1300, 720);
		bar.setVisible(false);
		MenuPanel.add(bar);
		MenuPanel.add(availableAmt);
		MenuPanel.add(updateAmt);
		MenuPanel.add(l1);
		MenuPanel.add(l2);
		MenuPanel.add(updateButton);
		MenuPanel.add(errorLabel);
		this.add(MenuPanel);
		blackJack.addActionListener(new blackJack());
		slotMachine.addActionListener(new slotMachine());
		poker.addActionListener(new poker());
		viewUrBalance.addActionListener(new viewUrBalance());
		updateBalance.addActionListener(new updateBalance());
		updateButton.addActionListener(new updateButton());
	    signOut.addActionListener(new signOut());
	}
	
	@Override
	public void run(){
		bar.setVisible(true);
		 try {
			  in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			  while (!in.ready()) {}
// Read one line and output it
			  email = in.readLine();
			  in.close();
	      }
	      catch(Exception e) {
	         e.printStackTrace();
	      }
	    	
	}
// User should be allowed to play all 3 games.
// If game is already running, don't start one more thread.
	class blackJack implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(DatabaseHandler.getBlackJackList().indexOf(email) < 0){
				DatabaseHandler.getBlackJackList().add(email);
				userGameInfo = new userGameInfo(email, 1); 
				Thread UG = new Thread(userGameInfo);
				UG.start();
			}else{
				errorLabel.setText("Game is already running.");
			}
		}
	}

	class slotMachine implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(DatabaseHandler.getSlotMachineList().indexOf(email) < 0){
				DatabaseHandler.getSlotMachineList().add(email);
				userGameInfo = new userGameInfo(email, 2); 
				Thread UG = new Thread(userGameInfo);
				UG.start();
			}else{
				errorLabel.setText("Game is already running.");
			}
		}
	}
	
	class poker implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(DatabaseHandler.getPokerList().indexOf(email) < 0){
				DatabaseHandler.getPokerList().add(email);
				userGameInfo = new userGameInfo(email, 0); 
				Thread UG = new Thread(userGameInfo);
				UG.start();
			}else{
				errorLabel.setText("Game is already running.");
			}
		}
	}

// Display user's current balance.	
	class viewUrBalance implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			l1.setVisible(true);
			availableBalance = DatabaseHandler.getAmount(email);
			availableAmt.setText(Integer.toString(availableBalance));
			availableAmt.setVisible(true);
			availableAmt.setEnabled(false);
		}
	}

// Allows user to update their balance. 	
	class updateBalance implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			l2.setVisible(true);
			updateAmt.setVisible(true);
			updateButton.setVisible(true);
		}
	}

// User sign out.	
	class signOut implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			DatabaseHandler.getUserNameList().remove(email);
			setVisible(false);
			dispose();
		}
	}
	
	public boolean isNumeric(String s) {  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	}

// Update the balance.	
	class updateButton implements ActionListener{
		public void actionPerformed(ActionEvent e){
			errorLabel.setText("");
			if(!isNumeric(updateAmt.getText())){
				errorLabel.setText("Enter Numeric value");
				updateAmt.setText("");				
			}else{
				availableBalance += Integer.parseInt(updateAmt.getText()); 
				availableAmt.setText(Integer.toString(availableBalance));
				updateAmt.setText("");
				try {
					String status = DatabaseHandler.updateAmount(Integer.toString(availableBalance), email);
					if(!status.equals("200")){
						errorLabel.setText(status);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
