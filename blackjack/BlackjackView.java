package blackjack;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import server.*;

public class BlackjackView extends JFrame{
	int jlabelCount;
	int dealerJlabelCount;
	private ArrayList<JLabel> dealerLabelList;
	private ArrayList<JLabel> labelList;
	private JLabel backCardLabel       = new JLabel("");
	private JLabel dealResult          = new JLabel("");
	private JTextField creditAvailable = new JTextField(20);
	private JTextField betField        = new JTextField(20);
	private JButton dealButton         = new JButton("Deal");
	private JButton hitButton          = new JButton("Hit");
	private JButton stayButton         = new JButton("Stay");
	private ImagePanel blackjackPanel  = new ImagePanel(new ImageIcon(getClass().getResource("/resources/" +"bg8.jpg")).getImage());
	private JLabel Credits 			   = new JLabel("Credits");
	private JLabel BET                 = new JLabel("BET");
	private JLabel playerName          = new JLabel("");
	
// Sets up the view and adds the components
	public BlackjackView(){
		int count = 300;
		blackjackPanel.setLayout(null);
		this.labelList = new ArrayList<JLabel>();
		this.dealerLabelList = new ArrayList<JLabel>();
		this.jlabelCount = 0;
		this.dealerJlabelCount = 0;
		for(int i = 0; i < 4; i++){
			dealerLabelList.add(new JLabel(""));
			dealerLabelList.get(i).setBounds(count, 40, 75, 100);
			blackjackPanel.add(dealerLabelList.get(i));
			count = count + 80;
		}
		backCardLabel.setBounds(250, 40, 175, 100);
		count = 300;
		for(int i = 0; i < 21; i++){
			labelList.add(new JLabel(""));
			labelList.get(i).setBounds(count, 160, 75, 100);
			blackjackPanel.add(labelList.get(i));
			count = count + 80;
		}
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
// On panel close (Blackjack game close), remove the user from gameNameList so that user can play it again.
// else it will throw message "Game is running".
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				DatabaseHandler.getBlackJackList().remove(playerName.getText());
				setVisible(false);
				dispose();
			  }
		});
		this.setSize(1300, 720);
		Credits.setForeground(Color.orange);
		BET.setForeground(Color.orange);
		Credits.setBounds(350, 300, 100, 30);
		creditAvailable.setBounds(410, 300, 100, 30);
		BET.setBounds(550, 300, 100, 30);
		betField.setBounds(610, 300, 100, 30);
		dealButton.setBounds(790, 300, 100, 30);
		hitButton.setBounds(770, 300, 100, 30);
		stayButton.setBounds(890, 300, 100, 30);
		dealResult.setBounds(550, 340, 500, 30);
		dealResult.setForeground(Color.red);
		blackjackPanel.add(playerName);
		blackjackPanel.add(backCardLabel);
		blackjackPanel.add(Credits);
		blackjackPanel.add(BET);
		blackjackPanel.add(betField);
		blackjackPanel.add(creditAvailable);
		blackjackPanel.add(dealButton);
		blackjackPanel.add(hitButton);
		playerName.setVisible(false);
		hitButton.setVisible(false);
		blackjackPanel.add(stayButton);
		stayButton.setVisible(false);
		creditAvailable.setEnabled(false);
		blackjackPanel.add(dealResult);	
		this.add(blackjackPanel);
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

// Add card images to label (for player).	
	public void setSecondOnwardCard(ImageIcon img){
		labelList.get(jlabelCount).setIcon(img);
		jlabelCount++;
	}
// Add card images to label (for dealer).	
	public void setDealerCard(ImageIcon img){
		dealerLabelList.get(dealerJlabelCount).setIcon(img);
		dealerJlabelCount++;
	}
	
	public void setDealerCardVisibility(boolean cardVisibility){
		ImageIcon img = new ImageIcon(getClass().getResource("/resources/" + "cardBack.gif"));
		backCardLabel.setIcon(img);
		dealerLabelList.get(1).setVisible(cardVisibility);
		backCardLabel.setVisible(!cardVisibility);
	}
	
	public void setButtonVisibility(boolean bVisibility){
		hitButton.setVisible(bVisibility);
		stayButton.setVisible(bVisibility);
		dealButton.setVisible(!bVisibility);
	}
	
// Display HandSum
	public void setResult(String result){
		dealResult.setText(result);
	}
	
	public void setCredits(String credits){
		creditAvailable.setText(credits);
	}
	
	public void setBet(){
		betField.setText("");
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
	
	public void setPlayerName(String name){
		playerName.setText(name);
	}
// If the Deal button is clicked execute a method in the Controller named actionPerformed
	void addDealListener(ActionListener listenForDealButton){
		dealButton.addActionListener(listenForDealButton);		
	}

// If the Hit button is clicked execute a method in the Controller named actionPerformed
	void addHitListener(ActionListener listenForHitButton){
		hitButton.addActionListener(listenForHitButton);		
	}

// If the Stay button is clicked execute a method in the Controller named actionPerformed	
	void addStayListener(ActionListener listenForStayButton){
		stayButton.addActionListener(listenForStayButton);		
	}
}
