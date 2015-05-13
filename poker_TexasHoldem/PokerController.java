package poker_TexasHoldem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import server.DatabaseHandler;

public class PokerController {
	private PokerView theView;
	private PokerModel theModel;
	private int bet = 0;
	private int credits = 0;
	private int dealerCount;
	private ArrayList<String> cards = new ArrayList<String>();
	private ArrayList<String> playerCards = new ArrayList<String>();
	private ArrayList<String> dealerCards = new ArrayList<String>();
	private ArrayList<String> handNameList = new ArrayList<String>(
	Arrays.asList("HighCard", "OnePair", "TwoPair", "ThreeOfAKind",  
			      "Straight", "Flush", "FullHouse", "FourOfAKind",
			      "StraightFlush"));
	public PokerController(PokerView theView, PokerModel theModel) {
		this.theView = theView;
		this.theModel = theModel;
		this.dealerCount = 0;
		this.theView.setCredits(theModel.getAmount());
		this.theView.setPlayerName(theModel.getPlayerName());
		this.theView.addDealListener(new DealListener());
		this.theView.addRaiseListener(new RaiseListener());
		this.theView.addAllInListener(new AllInListener());
		this.theView.addFoldListener(new FoldListener());
		this.theView.addCheckListener(new CheckListener());
	}

// Randomly select 4 cards : 2 for players and 2 for dealer.
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
			for(int i=0; i < 4; i++){
					theModel.randomCardNoCalculation(0, 51);
					if(i < 2){
						dealerCards.add(theModel.getRandomCardNo());
					}else{
						playerCards.add(theModel.getRandomCardNo());
					}				
					theView.setSecondOnwardCard(new ImageIcon(getClass().getResource("/resources/" + theModel.getRandomCardNo() + ".gif")));
			}
     		theView.changeVisibility();
     		theView.setDealerCardVisibility(false);
		}
	}

// Action performed on check button clicked.
// On first click it displays 3 cards followed by 1 followed by 1 and then gives the result.
	class CheckListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(dealerCount == 0){
				for(int i=0; i < 3; i++){
					theModel.randomCardNoCalculation(0, 51);
					cards.add(theModel.getRandomCardNo());
					theView.setDealerCard(new ImageIcon(getClass().getResource("/resources/" + theModel.getRandomCardNo() + ".gif")));
				}
			}else{
				theModel.randomCardNoCalculation(0, 51);
				cards.add(theModel.getRandomCardNo());
				theView.setDealerCard(new ImageIcon(getClass().getResource("/resources/" + theModel.getRandomCardNo() + ".gif")));
			}	
			if(dealerCount == 2){
				try {
     				compareHands();
					theView.setDealerCardVisibility(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				theView.reset();
			}
			dealerCount++;
		}
	}

// It allows user to raise his/her bet amount.
	class RaiseListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			bet = theView.getBet();
			credits = theView.raiseBet();
			if (credits <= 0){
				theView.setResult("Please enter amount to be raise");
				theView.raiseReset();
				return;
			}else{
				credits += bet;
				if(credits > theView.getCredits()){
					theView.setResult("Amount greater than available balance");
					theView.raiseReset();
					return;
				}else{
					theView.setBet(Integer.toString(credits));
					theView.raiseReset();
				}
			}
		}
	}

// All available amount be set as a bet.	
	class AllInListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			theView.setBet(Integer.toString(theView.getCredits()));
		}
	}
	
	class FoldListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
		}
	}

// It compares hands and returns the result.
	public void compareHands() throws SQLException{
		boolean playerStatus = theModel.checkForCardRank(playerCards, dealerCards);
		playerCards.addAll(cards);
		theModel.checkHand(playerCards);
		String pHandName = theModel.getHand();
		dealerCards.addAll(cards);
		theModel.checkHand(dealerCards);
		String dHandName = theModel.getHand();
		bet = theView.getBet();
		credits = theView.getCredits();
		if(handNameList.indexOf(pHandName) > handNameList.indexOf(dHandName)){
			theView.setResult("Won");
			credits += bet;
		}else if(handNameList.indexOf(pHandName) < handNameList.indexOf(dHandName)){
			theView.setResult("Lost");
			credits -= bet;
		}else{
			if(playerStatus){
				theView.setResult("Won");
				credits += bet;
			}else{
				theView.setResult("Lost");
				credits -= bet;
			}
		}
		
		theModel.updateAccount(Integer.toString(credits));
		DatabaseHandler.setAvailableAmt(credits);
		theView.setCredits(theModel.getAmount());
		theView.setBet("");
	}
	
// Clear the screen before every deal.	
	public void clear(){
		dealerCount = 0;
		theView.setResult("");
		if(!cards.isEmpty()){
			cards.clear();
			playerCards.clear();
			dealerCards.clear();
		}
		theView.setJlabelCount(0);
		for (JLabel JL : theView.getLabelList()) {
		    JL.setIcon(null);
		}
		theView.setDealerJlabelCount(0);
		for (JLabel JL : theView.getDealerLabelList()) {
		    JL.setIcon(null);
		}
	}
	
}