package server;
import poker_TexasHoldem.*;
import blackjack.*;
import slotmachine.*;

class userGameInfo implements Runnable{
	private String userName;
	private int gameSelected;
	
	userGameInfo(String userName, int gameSelected) {
		this.userName = userName;
		this.gameSelected = gameSelected;
	}

	@Override
	public void run(){
		switch(gameSelected){ 
// Start Poker game.
			case 0:	PokerView theView_P = new PokerView();
					PokerModel theModel_P = new PokerModel(userName);
					PokerController theController = new PokerController(theView_P,theModel_P);
					theView_P.setVisible(true);
					break;
// Start Blackjack game.        
			case 1:	BlackjackView theView_B = new BlackjackView();
					BlackjackModel theModel_B = new BlackjackModel(userName);
					BlackjackController theController_B = new BlackjackController(theView_B,theModel_B);
					theView_B.setVisible(true); 
					break;
// Start Slotmachine game.
			case 2:	SlotmachineView theView_S = new SlotmachineView();
					SlotmachineModel theModel_S = new SlotmachineModel(userName);
					SlotmachineController theController_S = new SlotmachineController(theView_S,theModel_S);
					theView_S.setVisible(true);
					break;
			default: System.out.println("Error: Wrong option");
		}
	}
}
