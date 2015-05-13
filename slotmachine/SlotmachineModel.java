package slotmachine;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import server.DatabaseHandler;

public class SlotmachineModel {
	private int randomCardValue;
	private int pairCount;
	private String playerName; 
	private String reelName;
    String[] reels = {"0","1","2","3","4","5","6","7","8","9"};
    HashMap<String, Integer> valueOfReels;
    
	public SlotmachineModel(String playerName){
		this.playerName = playerName;
		this.valueOfReels = new HashMap<String, Integer>(){{
			put("1",1);put("2",2);put("3",3);put("4",4);put("5",5);
			put("6",6);put("7",7);put("8",8);put("9",9);put("0",10);
		}};
	}   
	
	public String getPlayerName() {
		return playerName;
	}

	public String getReelName() {
		return reelName;
	}

	public void setReelName(String reelName) {
		this.reelName = reelName;
	}

	public void randomCardNoCalculation(int max, int min){
		randomCardValue = (int)(Math.random()*(max-min+1) + min);
	}
	
	public String getRandomCardNo(){
		return reels[randomCardValue];
	}

// Amount paid:
// if 0 same images = user loses bet money.
// If 2 same images = 2 percent of (bet * image value) 
// If 3 same images = 3 percent of (bet * image value)
// if jackpot = double the image value.
	public int trackOfBet(ArrayList<String> reels, int betAmount){
		int i = pairCount = 0;
		int value = 0;
		reelName = "false";
		boolean firstPairFound = false;
		Collections.sort(reels);
		
		for(i=0; i< reels.size()-1;i++){
			if(reels.get(i).equals(reels.get(i+1))){
				if(firstPairFound){
					pairCount = 0;
				}
				pairCount++; 
				reelName = reels.get(i);
			}else{
				if(pairCount == 1){ 
					firstPairFound = true;
				}
			}
		}
		if(!reelName.equals("false")){
			value = valueOfReels.get(reelName);
		}
		
		switch(pairCount){
			case 1:	betAmount = Math.round(betAmount * value * 2 /100);
					break;
			case 2: betAmount = Math.round(betAmount *value * 3/100);
					break;
			case 3: betAmount = betAmount + value * 2;
					break;
			default: betAmount = 0;
		}
		return betAmount;
	}

// Update user's balance.	
	public void updateAccount(String creditsAvailable) throws SQLException{
		DatabaseHandler.updateAmount(creditsAvailable, playerName);
	}
	
// Get updated balance.	
	public int getAmount(){
		return DatabaseHandler.getAmount(playerName);
	}
}
