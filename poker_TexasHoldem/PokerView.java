package poker_TexasHoldem;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;
import server.DatabaseHandler;
import server.ImagePanel;

public class PokerView extends JFrame{
	int jlabelCount;
	int dealerJlabelCount;
	private ArrayList<JLabel> dealerLabelList;
	private ArrayList<JLabel> labelList;
	private JLabel dealResult 		   = new JLabel("");
	private JTextField creditAvailable = new JTextField(20);
	private JTextField betField 	   = new JTextField(20);
	private JTextField raiseField 	   = new JTextField(20);
	private JButton dealButton 		   = new JButton("Deal");
	private JLabel Credits 			   = new JLabel("Credits");
	private JButton checkButton 	   = new JButton("Check");
	private JButton foldButton 		   = new JButton("Fold");
	private JButton raiseButton 	   = new JButton("Raise");
	private JButton allInButton        = new JButton("All In");
	private JLabel BET 				   = new JLabel("BET");
	private JLabel cardBack_1		   = new JLabel("");
	private JLabel cardBack_2		   = new JLabel("");
	private ImagePanel pokerPanel      = new ImagePanel(new ImageIcon(getClass().getResource("/resources/" +"bg8.jpg")).getImage());
	private JLabel playerName          = new JLabel("");

// Sets up the view and adds the components
	public PokerView(){
		cardBack_1.setBounds(350, 40, 175, 100);
		cardBack_2.setBounds(310, 40, 175, 100);
		int count = 350;
		pokerPanel.setLayout(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
// On panel close (poker game close), remove the user from gameNameList so that user can play it again.
// else it will throw message "Game is running".
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				DatabaseHandler.getPokerList().remove(playerName.getText());
				setVisible(false);
				dispose();
			  }
		});
		this.setSize(1300, 720);
		this.dealerLabelList = new ArrayList<JLabel>();
		this.dealerJlabelCount = 0;
		for(int i = 0; i < 5; i++){
			dealerLabelList.add(new JLabel(""));
			dealerLabelList.get(i).setBounds(count, 160, 75, 100);
			pokerPanel.add(dealerLabelList.get(i));
			count = count + 80;
		}
		count = 350;
		this.labelList = new ArrayList<JLabel>();
		this.jlabelCount = 0;
		for(int i = 0; i < 2; i++){
			labelList.add(new JLabel(""));
			labelList.get(i).setBounds(count, 40, 75, 100);
			pokerPanel.add(labelList.get(i));
			count = count + 80;
		}
		count = 350;
		for(int i = 2; i < 5; i++){
			labelList.add(new JLabel(""));
			labelList.get(i).setBounds(count, 280, 75, 100);
			pokerPanel.add(labelList.get(i));
			count = count + 80;
		}
		creditAvailable.setEnabled(false);
		Credits.setForeground(Color.orange);
		BET.setForeground(Color.orange);
		Credits.setBounds(350, 390, 100, 30);
		creditAvailable.setBounds(410, 390, 100, 30);
		BET.setBounds(560, 390, 100, 30);
		betField.setBounds(620, 390, 100, 30);
		dealButton.setBounds(770, 390, 100, 30);
		checkButton.setBounds(350, 430, 100, 30);
		raiseButton.setBounds(460, 430, 100, 30);
		raiseField.setBounds(565, 430, 90, 30);
		allInButton.setBounds(660, 430, 100, 30);
		foldButton.setBounds(770, 430, 100, 30);
		dealResult.setBounds(460, 480, 500, 30);
		playerName.setVisible(false);
		checkButton.setEnabled(false);
		raiseButton.setEnabled(false);
		allInButton.setEnabled(false);
		foldButton.setEnabled(false);
		raiseField.setEnabled(false);
		dealResult.setForeground(Color.red);
		pokerPanel.add(playerName);
		pokerPanel.add(cardBack_1);
		pokerPanel.add(cardBack_2);
		pokerPanel.add(checkButton);
		pokerPanel.add(raiseButton);
		pokerPanel.add(allInButton);
		pokerPanel.add(Credits);
		pokerPanel.add(creditAvailable);
		pokerPanel.add(BET);
		pokerPanel.add(betField);
		pokerPanel.add(dealButton);
		pokerPanel.add(dealResult);	
		pokerPanel.add(raiseField);	
		this.add(pokerPanel);
	}
	
	public int getDealerJlabelCount() {
		return dealerJlabelCount;
	}

	public void setDealerJlabelCount(int dealerJlabelCount) {
		this.dealerJlabelCount = dealerJlabelCount;
	}

	public ArrayList<JLabel> getDealerLabelList() {
		return dealerLabelList;
	}

	public void setDealerLabelList(ArrayList<JLabel> dealerLabelList) {
		this.dealerLabelList = dealerLabelList;
	}

	public int getJlabelCount() {
		return jlabelCount;
	}

	public void setJlabelCount(int jlabelCount) {
		this.jlabelCount = jlabelCount;
	}

	public ArrayList<JLabel> getLabelList() {
		return labelList;
	}

	public void setLabelList(ArrayList<JLabel> labelList) {
		this.labelList = labelList;
	}

	public void setSecondOnwardCard(ImageIcon img){
		labelList.get(jlabelCount).setIcon(img);
		jlabelCount++;
	}
	
	public void setDealerCard(ImageIcon img){
		dealerLabelList.get(dealerJlabelCount).setIcon(img);
		dealerJlabelCount++;
	}

	public void setDealerCardVisibility(boolean cardVisibility){
		ImageIcon img = new ImageIcon(getClass().getResource("/resources/" + "cardBack.gif"));
		cardBack_1.setIcon(img);
		cardBack_2.setIcon(img);
		cardBack_1.setVisible(!cardVisibility);
		cardBack_2.setVisible(!cardVisibility);
		labelList.get(0).setVisible(cardVisibility);
		labelList.get(1).setVisible(cardVisibility);
	}
	
// Display Final result - Whether user lost or won.
	public void setResult(String result){
		dealResult.setText(result);
	}

// Update available credit.
	public void setCredits(String credits){
		creditAvailable.setText(credits);
	}

// Field to place a bet.
	public void setBet(String bet){
		betField.setText(bet);
	}
	
// Set player name.
	public void setPlayerName(String name){
		playerName.setText(name);
	}
	
// Check if bet is numeric.
	public int getBet(){
		if(betField.getText().matches(".*[1-9].*")){
			return Integer.parseInt(betField.getText());
		}
		return 0;
	}
	
	public int getCredits(){
		return Integer.parseInt(creditAvailable.getText());
	}
	
	public void changeVisibility(){
		betField.setEnabled(false);
		dealButton.setEnabled(false);
		checkButton.setEnabled(true);
		raiseButton.setEnabled(true);
		allInButton.setEnabled(true);
		foldButton.setEnabled(true);
		raiseField.setEnabled(true);;
	}
	
	public int raiseBet(){
		if(raiseField.getText().matches(".*[1-9].*")){
			return Integer.parseInt(raiseField.getText());
		}
		return 0;
	}
	
	public void raiseReset(){
		raiseField.setText("");
	}
	
	public void reset(){
		raiseField.setEnabled(false);
		betField.setEnabled(true);
		dealButton.setEnabled(true);
		betField.setText("");
		checkButton.setEnabled(false);
		raiseButton.setEnabled(false);
		allInButton.setEnabled(false);
		foldButton.setEnabled(false);
	}

// Below code will particular operations on the button click. 
	void addDealListener(ActionListener listenForDealButton){
		dealButton.addActionListener(listenForDealButton);		
	}
	
	void addCheckListener(ActionListener listenForCheckButton){
		checkButton.addActionListener(listenForCheckButton);		
	}
	
	void addRaiseListener(ActionListener listenForRaiseButton){
		raiseButton.addActionListener(listenForRaiseButton);		
	}
	
	void addAllInListener(ActionListener listenForAllInButton){
		allInButton.addActionListener(listenForAllInButton);		
	}
	
	void addFoldListener(ActionListener listenForfoldButton){
		foldButton.addActionListener(listenForfoldButton);		
	}

}
