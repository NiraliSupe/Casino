package blackjack;
import java.sql.SQLException;
import java.util.Arrays;
import server.DatabaseHandler;

public class BlackjackModel {
	private int randomCardValue;
	private String playerName;
	private int noOfAces = 0;
	private int playerNoOfAces = 0;
	private int dalerNoOfAces = 0;
	private int dealerhandSum = 0;
	private int playerhandSum = 0;
	private String result;
	String[] deck = 
	   {
	    "1ace_S","2_S","3_S","4_S","5_S","6_S","7_S","8_S","9_S","a10_S","bjack_S","cqueen_S","king_S",
	    "1ace_H","2_H","3_H","4_H","5_H","6_H","7_H","8_H","9_H","a10_H","bjack_H","cqueen_H","king_H",
	    "1ace_D","2_D","3_D","4_D","5_D","6_D","7_D","8_D","9_D","a10_D","bjack_D","cqueen_D","king_D",
	    "1ace_C","2_C","3_C","4_C","5_C","6_C","7_C","8_C","9_C","a10_C","bjack_C","cqueen_C","king_C" 
	   };
	String[] cardNo = {"1ace", "2", "3", "4", "5", "6", "7", "8", "9", "a10", "bjack", "cqueen", "king"};
	
	public BlackjackModel(String PlayerName){
		this.playerName = PlayerName;
		this.dealerhandSum = 0;
		this.playerhandSum = 0;
	}   
	
	public String getPlayerName() {
		return playerName;
	}

	public int getNoOfAces() {
		return noOfAces;
	}
	
	public void setNoOfAces(int noOfAces) {
		this.noOfAces = noOfAces;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public void randomCardNoCalculation(int max, int min){
		randomCardValue = (int)(Math.random()*(max-min+1) + min);
	}
	
	public String getRandomCardNo(){
		return deck[randomCardValue];
	}
	
	public int getDealerhandSum() {
		return dealerhandSum;
	}
	
	public void setDealerhandSum(int dealerhandSum) {
		this.dealerhandSum = dealerhandSum;
	}
	
	public int getPlayerhandSum() {
		return playerhandSum;
	}
	
	public void setPlayerhandSum(int playerhandSum) {
		this.playerhandSum = playerhandSum;
	}

// Calculate hand sum for dealer.	
	public void calDealerHandSum(String cardName){
		dealerhandSum = calculateHandSum(cardName, dealerhandSum, "dealer");
	}

// Calculate hand sum for player.	
	public void calPlayerHandSum(String cardName){
		playerhandSum = calculateHandSum(cardName, playerhandSum, "player");
	}

// Hand sum calculation. Face cards are worth 10 points.
// Ace is 1 or 11 depends on the hand sum. 
// 2 - 10 cards has value 2 to 10 respectively.
	public int calculateHandSum(String cardName, int handSum, String playerType){
		if(playerType.equals("dealer")){
			noOfAces = dalerNoOfAces;
		}else{
			noOfAces = playerNoOfAces;
		}
		int cardNumber = 0;
		cardNumber = Arrays.asList(cardNo).indexOf(cardName.split("_")[0]);
		if(cardNumber == 0){
			handSum += 11;
			noOfAces++;
		}else if(cardNumber > 9 && cardNumber < 13){
			handSum += 10;
		}else{
			cardNumber++;
			handSum += cardNumber;
		}

// Add 11 to sum if it's Ace, but if sum is greater than 21, add one instead of 11.
		if(handSum > 21 && noOfAces > 0){
			handSum -= 10;
			noOfAces--;
		}
		if(playerType.equals("dealer")){
			dalerNoOfAces = noOfAces;
		}else{
			playerNoOfAces = noOfAces;
		}
		return handSum;
	}
	

	public String resultOnHitOrStay(){
		if(playerhandSum > 21){
			result = "Busted";
			return result;
		}
		
		if(dealerhandSum > playerhandSum){
			result = "Lose";
			return result;
		}
		result = "Win";
		return result;
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
