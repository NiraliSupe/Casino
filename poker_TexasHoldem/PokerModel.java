package poker_TexasHoldem;
import java.sql.SQLException;
import java.util.*;

import server.*;

public class PokerModel {
	private int randomCardValue;
	private String hand = null;
	private String playerName; 
	private int[] handCards;
	private int flush =0;
	private int straight =0;
	private int pairCount = 0;
	private int cnt;
	private ArrayList<String> deck = new ArrayList<String>(Arrays.asList
	   ("1ace_S","2_S","3_S","4_S","5_S","6_S","7_S","8_S","9_S","a10_S","bjack_S","cqueen_S","king_S",
	    "1ace_H","2_H","3_H","4_H","5_H","6_H","7_H","8_H","9_H","a10_H","bjack_H","cqueen_H","king_H",
	    "1ace_D","2_D","3_D","4_D","5_D","6_D","7_D","8_D","9_D","a10_D","bjack_D","cqueen_D","king_D",
	    "1ace_C","2_C","3_C","4_C","5_C","6_C","7_C","8_C","9_C","a10_C","bjack_C","cqueen_C","king_C" 
	   ));
	String[] cardNo = {"1ace", "2", "3", "4", "5", "6", "7", "8", "9", "a10", "bjack", "cqueen", "king"};
	
	public PokerModel(String playerName){
		this.playerName = playerName;
		this.handCards = new int[7];
	}   
	
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public void randomCardNoCalculation(int max, int min){
		randomCardValue = (int)(Math.random()*(max-min+1) + min);
	}
	
	public String getRandomCardNo(){
		return deck.get(randomCardValue);
	}

//handcards array holds Hand information. 0, 1, 2 : card pair (There can be at most 3 pairs)
// 3 - threeOfKind , 4 - fourOfKind, 5 - straight, 6 - flush
// This information will be used to highlight the winning hand at the last.
	public void checkHand(ArrayList<String> cards){	
		int i = 0;
		cnt = pairCount = straight = flush  = 0;
	
		Arrays.fill(handCards, 0);
		Collections.sort(cards);
		hand = null;
		for(i=0; i< cards.size()-1;i++){
				String firstCardNo = cards.get(i).split("_")[0];
				String secondCardNo = cards.get(i+1).split("_")[0];
				if(firstCardNo.equals(secondCardNo)){
					pairCount++;
				}else{
					checkForPair(i);
					int valueOfFirstCard = Arrays.asList(cardNo).indexOf(firstCardNo)+ 1;
					if(cardNo[valueOfFirstCard].equals(secondCardNo)){
						straight++;
						handCards[5]= i+1;
					}else if(straight < 4){
						straight = 0;
						handCards[5]= 0;
					}
				
					if(cards.get(i).split("_")[1].equals(cards.get(i+1).split("_")[1])){
						flush++;
						handCards[6]= i+1;
					}else if(flush < 4){
						flush = 0;
						handCards[6]= 0;
					}
				
				}
		}
		checkForPair(i);
		checkForKindOfPair();
	}

// If pairCount is 1, means there are two matching cards, if 2, there are three matching cards and so on.
	public void checkForPair(int i){
		if(pairCount == 1){
			handCards[cnt]= i;
			cnt++;
		}else if(pairCount == 2){
			handCards[3] = i;
		}else if(pairCount == 3){
			handCards[4] = i;
		}
		pairCount = 0;
	}

	public String checkForKindOfPair(){
		if(handCards[0] != 0){
			hand = "OnePair";
		}
		if(handCards[0] != 0 && handCards[1] != 0){
			hand = "TwoPair";
		}
		if(handCards[3] != 0){
			hand = "ThreeOfAKind";
		}
		if(handCards[4] != 0){
			hand = "FourOfAKind";
		}	
		if(handCards[3] != 0 && handCards[0] != 0){
			hand = "FullHouse";
		}
		if(flush >= 4){
			hand = "Flush";
		}else if(straight >= 4){
			hand = "Straight";
		}else if ((flush >= 4)&&(straight >= 4)){
			hand = "StraightFlush";
		}else if(hand == null){
			hand = "HighCard";
		}
		return hand;
	}
	
	public String getHand(){
		return hand;
	}

// True when player wins. False when dealer wins.
// This method executes to check whether player has high rank cards or not.
	public boolean checkForCardRank(ArrayList<String> pcards, ArrayList<String> dcards){
		int pFirstCardNo  = Arrays.asList(cardNo).indexOf(pcards.get(0).split("_")[0]);
		int pSecondCardNo = Arrays.asList(cardNo).indexOf(pcards.get(1).split("_")[0]);
		int dFirstCardNo  = Arrays.asList(cardNo).indexOf(dcards.get(0).split("_")[0]);
		int dSecondCardNo = Arrays.asList(cardNo).indexOf(dcards.get(1).split("_")[0]);
		if(dFirstCardNo == 0){
			dFirstCardNo = 26;
		}else if(dSecondCardNo == 0){
			dSecondCardNo = 26;
		}else if(pFirstCardNo == 0){
			pFirstCardNo = 26;
		}else if(pSecondCardNo == 0){
			pSecondCardNo = 26;
		}
		int playerTotal = pFirstCardNo + pSecondCardNo;
		int dealerTotal = dFirstCardNo + dSecondCardNo;
		if(playerTotal > dealerTotal){
			return true;
		}else{
			return false;
		}
	}

// Update user's balance.	
	public void updateAccount(String creditsAvailable) throws SQLException{
		DatabaseHandler.updateAmount(creditsAvailable, playerName);
	}

// Get updated balance.
	public String getAmount(){
		return Integer.toString(DatabaseHandler.getAmount(playerName));
	}
}
