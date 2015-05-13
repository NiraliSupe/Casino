package slotmachine;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import server.DatabaseHandler;

public class SlotmachineController {
	private SlotmachineView theView;
	private SlotmachineModel theModel;
	private ArrayList<String> reels = new ArrayList<String>();
	public SlotmachineController(SlotmachineView theView, SlotmachineModel theModel) {
		this.theView = theView;
		this.theModel = theModel;
		this.theView.setCreditResult(theModel.getAmount());
		this.theView.setPlayerName(theModel.getPlayerName());
		this.theView.addSpinReelsListener(new SpinReelsListener());
	}

// It spins the reel and displays the result.
	class SpinReelsListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			clear();
			int bet = theView.getBetAmount();
			int credits = theView.getCreditResult();
			if ((bet <= 0) || (bet > credits)){
				clear();
				theView.setResult("Amount hasn't entered or Amount greater than available amount");
				return;
			}else if(bet%10 != 0){
				clear();
				theView.setResult("Bet Amount should be multiple of 10");
				return;
			}
			for(int i=0; i < 4; i++){
				theModel.randomCardNoCalculation(0, 9);
				reels.add(theModel.getRandomCardNo());
				theView.setReelsDisplay(new ImageIcon(getClass().getResource("/resources/" + theModel.getRandomCardNo() + ".png")));
        	}
			int WinAmount = theModel.trackOfBet(reels, bet);
			theView.setWinResult(Integer.toString(WinAmount));
			theView.setPaidResult(Integer.toString(WinAmount));
			int totalCredit = credits + WinAmount - bet;
			try {
				theModel.updateAccount(Integer.toString(totalCredit));
				DatabaseHandler.setAvailableAmt(totalCredit);
				theView.setCreditResult(theModel.getAmount()); 
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			theView.setBetResult("");
		}
	}
	
// clear the screen before new deal.
	public void clear(){
		if(!reels.isEmpty()){
			reels.clear();
		}
		theView.setJlabelCount(0);
		theView.setWinResult("");
		theView.setPaidResult("");
		theView.setResult("");
		for (JLabel JL : theView.getLabelList()) {
		    JL.setIcon(null);
		}
	}
}
