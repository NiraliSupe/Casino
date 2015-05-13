package slotmachine;
import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import server.*;

public class SlotmachineView extends JFrame{
	int jlabelCount;
	private ArrayList<JLabel> labelList;
	private JLabel winLabel     	= new JLabel("Win");
	private JLabel paidLabel    	= new JLabel("Paid");
	private JLabel creditsLabel 	= new JLabel("Credit");
	private JLabel betLabel     	= new JLabel("BET");
	private JTextField winResult    = new JTextField(10);
	private JTextField paidResult   = new JTextField(10);
	private JTextField creditResult = new JTextField(10);
	private JTextField betResult    = new JTextField(10);
	private JLabel result       	= new JLabel("");
	private JButton SpinReelsButton = new JButton("Spin Reels");
	private JLabel playerName       = new JLabel("");
	private ImagePanel slotmachinePanel    = new ImagePanel(new ImageIcon(getClass().getResource("/resources/" +"bg8.jpg")).getImage());

// Sets up the view and adds the components
	public SlotmachineView() {
		slotmachinePanel.setLayout(null);
		int count = 350;
		this.labelList = new ArrayList<JLabel>();
		this.jlabelCount = 0;
		for(int i = 0; i < 4; i++){
			labelList.add(new JLabel(""));
			labelList.get(i).setBounds(count, 130, 350, 100);
			slotmachinePanel.add(labelList.get(i));
			count = count + 130;
		}
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
// On panel close (slotmachine game close), remove the user from gameNameList so that user can play it again.
// else it will throw message "Game is running".
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				DatabaseHandler.getSlotMachineList().remove(playerName.getText());
				setVisible(false);
				dispose();
			  }
		});
		this.setSize(1300, 720);
		winLabel.setForeground(Color.orange);
		paidLabel.setForeground(Color.orange);
		creditsLabel.setForeground(Color.orange);
		betLabel.setForeground(Color.orange);
		winLabel.setBounds(350, 300, 100, 30);
		winResult.setBounds(380, 300, 100, 30);
		paidLabel.setBounds(500, 300, 100, 30);
		paidResult.setBounds(540, 300, 100, 30);
		creditsLabel.setBounds(660, 300, 100, 30);
		creditResult.setBounds(720, 300, 100, 30);
		betLabel.setBounds(830, 300, 100, 30);
		betResult.setBounds(880, 300, 100, 30);
		SpinReelsButton.setBounds(540, 340, 100, 30);
		result.setBounds(660, 340, 500, 30);
		creditResult.setEnabled(false);
		winResult.setEnabled(false);
		paidResult.setEnabled(false);
		playerName.setVisible(false);
		result.setForeground(Color.RED);
		slotmachinePanel.add(playerName);
		slotmachinePanel.add(winLabel);
		slotmachinePanel.add(winResult);
		slotmachinePanel.add(paidLabel);
		slotmachinePanel.add(paidResult);
		slotmachinePanel.add(creditsLabel);
		slotmachinePanel.add(creditResult);
		slotmachinePanel.add(betLabel);
		slotmachinePanel.add(betResult);
		slotmachinePanel.add(SpinReelsButton);	
		slotmachinePanel.add(result);	
		this.add(slotmachinePanel);
	}
	
	public JButton getSpinReelsButton() {
		return SpinReelsButton;
	}

	public ArrayList<JLabel> getLabelList() {
		return labelList;
	}

	public void setLabelList(ArrayList<JLabel> labelList) {
		this.labelList = labelList;
	}
// Add images to label.
	public void setReelsDisplay(ImageIcon img){
		labelList.get(jlabelCount).setIcon(img);
		jlabelCount++;
	}
	
	public int getJlabelCount() {
		return jlabelCount;
	}

	public void setJlabelCount(int jlabelCount) {
		this.jlabelCount = jlabelCount;
	}
	
	public void setWinResult(String winResult) {
		this.winResult.setText(winResult);
	}

	public void setPaidResult(String paidResult) {
		this.paidResult.setText(paidResult);
	}

	public void setCreditResult(int creditResult) {
		this.creditResult.setText(Integer.toString(creditResult));
	}

	public void setBetResult(String betResult) {
		this.betResult.setText(betResult);
	}

	public void setPlayerName(String name){
		playerName.setText(name);
	}
	
// Display result 
	public void setResult(String result){
		this.result.setText(result);
	}
// check if bet is  numeric	
    public int getBetAmount(){
    	if(betResult.getText().matches(".*[1-9].*")){
			return Integer.parseInt(betResult.getText());
		}
		return 0;
    }

	public int getCreditResult(){
		return Integer.parseInt(creditResult.getText());
	}
	
	public int getPaidResult(){
		return Integer.parseInt(paidResult.getText());
	}
	
	public int getWinResult(){
		return Integer.parseInt(winResult.getText());
	}
	
// If the Deal button is clicked execute a method in the Controller named actionPerformed
	void addSpinReelsListener(ActionListener listenForSpinReelsButton){
		SpinReelsButton.addActionListener(listenForSpinReelsButton);		
	}
}
