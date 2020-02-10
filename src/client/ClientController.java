package client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * Controls the UI for the main game
 * @author Sean Barton
 *
 */
public class ClientController {
	
	private ClientView gameView;
	private ClientPlayer player;
	private PrintWriter socketOut;
	private BufferedReader socketIn;
//	private ClientController opponent;
	
	private final String EOM = "EOM";
	
	/**
	 * Constructs the controller for the game
	 * @param gameView the UI for the game
	 * @param player the player backend
	 */
	public ClientController(ClientView gameView, ClientPlayer player,
			PrintWriter socketOut, BufferedReader socketIn) {
		this.gameView = gameView;
		this.player = player;
		this.setSocketIn(socketIn);
		this.setSocketOut(socketOut);
		
		this.gameView.setMarkOutput(player.getMark());
		
		gameView.addButton00Listener(new Button00());
		gameView.addButton01Listener(new Button01());
		gameView.addButton02Listener(new Button02());
		gameView.addButton10Listener(new Button10());
		gameView.addButton11Listener(new Button11());
		gameView.addButton12Listener(new Button12());
		gameView.addButton20Listener(new Button20());
		gameView.addButton21Listener(new Button21());
		gameView.addButton22Listener(new Button22());
		gameView.addTextFieldListener(new TextFieldInput());

	}
	
	// *** GETTERS/SETTERS ***
	public void setViewMessage(String message) {
		gameView.setMessageOutput(message);
	}
	
	public String getPlayerName() {
		return player.getName();
	}
	
//	public void setOpponent(ClientController opponent) {
//		this.opponent = opponent;
//		player.setOpponent(opponent.player);
//	}
	
	public void setViewMark(char mark) {
		gameView.setMarkOutput(mark);
	}
	
	public void setPlayerMark(char mark) {
		player.setMark(mark);
	}
	
	public void setButtonDisabled(int row, int col) {
		gameView.setButtonDisabled(row, col);
	}
	
	public void setAllButtonsEnabled(boolean value) {
		gameView.setButtonsEnabled(value);
	}
	
	public void setButtonMark(int row, int col, char mark) {
		gameView.setButtonMark(row, col, mark);
	}
	
	/**
	 * Controls the passing of play between controllers
	 */
//	public void play() {
//		String gameResult = player.play();
//		if (gameResult != null) {
//			setViewMessage(gameResult);
//			opponent.setViewMessage(gameResult);
//			setAllButtonsEnabled(false);
//			opponent.setAllButtonsEnabled(false);
//			return;
//		}
//		
//		this.setEnabled(true);
//		opponent.setEnabled(false);
//		opponent.setViewMessage("Waiting for player " + player.getMark());
//		gameView.setMessageOutput("Make your move");
//		
//	}
	
	// *** HELPER METHODS ***
	private void buttonAction(int row, int col) {
//		player.getBoard().addMark(row, col, player.getMark());
		setButtonMark(row, col, player.getMark());
		setButtonDisabled(row, col);
		socketOut.println(Integer.toString(row) + Integer.toString(col));
//		socketOut.println(EOM);
		
//		opponent.setButtonMark(row, col, player.getMark());
//		opponent.setButtonDisabled(row, col);
//		player.getBoard().display();
//		opponent.play();
	}
	
	/**
	 * Enables or disables the game view GUI
	 * @param value true to enable, false otherwise
	 */
	public void setEnabled(boolean value) {
		gameView.setEnabled(value);
	}
	
	/**
	 * Enables or disables the buttons of the game GUI
	 * @param value true to enable, false otherwise
	 */
	public void setButtonsEnabled(boolean value) {
		gameView.setButtonsEnabled(value);
	}
	
//	public PrintWriter getSocketOut() {
//		return socketOut;
//	}

	protected void setSocketOut(PrintWriter socketOut) {
		this.socketOut = socketOut;
	}

//	public BufferedReader getSocketIn() {
//		return socketIn;
//	}

	protected void setSocketIn(BufferedReader socketIn) {
		this.socketIn = socketIn;
	}

	// *** Button Listener Classes ***
	class Button00 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("Button00 pressed");
			buttonAction(0, 0);
		}	
	}
	
	class Button01 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("Button01 pressed");
			buttonAction(0, 1);
		}	
	}
	
	class Button02 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("Button02 pressed");
			buttonAction(0, 2);
		}	
	}

	class Button10 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("Button10 pressed");
			buttonAction(1, 0);
		}	
	}

	class Button11 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("Button11 pressed");
			buttonAction(1, 1);
		}	
	}
	
	class Button12 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("Button12 pressed");
			buttonAction(1, 2);
		}	
	}

	class Button20 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("Button20 pressed");
			buttonAction(2, 0);
		}	
	}

	class Button21 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("Button21 pressed");
			buttonAction(2, 1);
		}	
	}
	
	class Button22 implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("Button22 pressed");
			buttonAction(2, 2);
		}	
	}
	
	class TextFieldInput implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			player.setName(gameView.getNameInput());
			gameView.setNameInputNonEditable();
			
		}		
	}
}
