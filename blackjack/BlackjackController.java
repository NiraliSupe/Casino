package blackjack;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import server.DatabaseHandler;

public class BlackjackController {
	private BlackjackView theView;
	private BlackjackModel theModel;
	private int bet = 0;
	private int credits = 0;
	
	public BlackjackController(BlackjackView theView, BlackjackModel theModel) {
		this.theView = theView;
		this.theModel = theModel;
		this.theView.setCredits(theModel.getAmount());
		this.theView.setPlayerName(theModel.getPlayerName());
		this.theView.addDealListener(new DealListener());
		this.theView.addHitListener(new HitListener());
		this.theView.addStayListener(new StayListener());
	}

// Initially display two cards on deal button. These cards are selected randomly.
	class DealListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			clear();
			bet = theView.getBet();
			credits = theView.getCredits();
			if ((bet <= 0)|| (bet > credits)){
				clear();
				theView.setResult("Amount hasn't entered or Amount greater than available amount");
				return;
			}else if(bet%10 != 0){
				clear();
				theView.setResult("Bet Amount should be multiple of 10");
				return;
			}
			for(int i = 0; i < 2; i++){
				theModel.randomCardNoCalculation(0, 51);
				theView.setSecondOnwardCard(new ImageIcon(getClass().getResource("/resources/" + theModel.getRandomCardNo() + ".gif")));
				theModel.calPlayerHandSum(theModel.getRandomCardNo());
			}
			
			for(int i = 0; i < 2; i++){
				theModel.randomCardNoCalculation(0, 51);
				theView.setDealerCard(new ImageIcon(getClass().getResource("/resources/" + theModel.getRandomCardNo() + ".gif")));
				theModel.calDealerHandSum(theModel.getRandomCardNo());
			}
			theView.setDealerCardVisibility(false);
			theView.setButtonVisibility(true);
		}
	}

// On hitButton press, randomly get the new card and calculate the hand sum.
// If handSum is greater than 21, the game will be over and disable the Hit Button.
	class HitListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			theModel.randomCardNoCalculation(0, 51);
			theView.setSecondOnwardCard(new ImageIcon(getClass().getResource("/resources/" + theModel.getRandomCardNo() + ".gif")));
			theModel.calPlayerHandSum(theModel.getRandomCardNo());
			if(theModel.resultOnHitOrStay().equals("Busted")){
				theView.setResult("Busted   "+Integer.toString(theModel.getPlayerhandSum()));
				theView.setDealerCardVisibility(true);
				credits -= bet;
				theView.setBet();
				theView.setButtonVisibility(false);
				try {
					theModel.updateAccount(Integer.toString(credits));
					DatabaseHandler.setAvailableAmt(credits);
					theView.setCredits(theModel.getAmount());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

// On stayButton press, compare the hand sums. Person with the greater hand sum wins the bet.
	class StayListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
            theView.setDealerCardVisibility(true);
            try {
            	compareResult();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            theView.setButtonVisibility(false);
			theView.setBet();
		}
	}

// Compare hand sum. If hand sum greater than 21 then display busted.
// else who has the greater hand sum wins the bet.
	public void compareResult() throws SQLException{
		if(theModel.resultOnHitOrStay().equals("Busted")){
			theView.setResult("Busted   "+Integer.toString(theModel.getPlayerhandSum()));
			credits -= bet;
		}else if(theModel.resultOnHitOrStay().equals("Lose")){
			theView.setResult("You Lost " + Integer.toString(theModel.getPlayerhandSum()));
			credits -= bet;
		}else {
			theView.setResult("You Won :D " + Integer.toString(theModel.getPlayerhandSum()));
			credits += bet;
		}
		theModel.updateAccount(Integer.toString(credits));
		DatabaseHandler.setAvailableAmt(credits);
		theView.setCredits(theModel.getAmount());
	}
	
// clear the screen before new deal.
	public void clear(){
		theModel.setPlayerhandSum(0);
		theModel.setDealerhandSum(0);
		theModel.setNoOfAces(0);
		theView.setJlabelCount(0);
		theView.setDealerJlabelCount(0);
		for (JLabel JL : theView.getLabelList()) {
		    JL.setIcon(null);
		}
		for (JLabel JL : theView. getDealerLabelList()) {
		    JL.setIcon(null);
		}
	}
}