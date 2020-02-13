package server;
import java.io.*;

/**
 * The server side player
 * @author Sean Barton
 *
 */
public class Player {

	private String name;
	private Board board;
	private Player opponent;
	private char mark;
	private BufferedReader socketIn;
	private PrintWriter socketOut;
	
	private final String EOM = "EOM";
	
	/**
	 * Constructs the player given a name and the assigned mark
	 * @param name the name of the player
	 * @param mark the player's mark ('X' or 'O')
	 */
	public Player(String name, char mark, BufferedReader socketIn, PrintWriter socketOut) {
		this.name = name;
		this.mark = mark;
		this.socketIn = socketIn;
		this.socketOut = socketOut;
	}
	
	/**
	 * Determines if the game has been won, lost or tied. This method displays
	 * the board and any updates and transfers play back an forth between players.
	 * @throws IOException
	 */
	public void play() {
		System.out.println();
		String showBoard = board.display();
		System.out.println(showBoard);
		if (!(board.xWins() || board.oWins() || board.isFull())) {
			writeMessage("enable");
			writeMessage("Take your turn");
			opponent.writeMessage("Player " + mark + "'s turn...");
			String coords = null;
			try {
				coords = this.makeMove();
			} catch (IOException e) {
				e.printStackTrace();
			}
			updateOpponent(coords, mark);
		}
		else {
			if (board.isFull()){
				if(board.xWins() || board.oWins()) {
					printWinner();
				}
				else {
					endingMessage("The game is tied!");
				}
			}
			else {
				printWinner();
			}
			writeMessage("disableAllButtons");
			opponent.writeMessage("disableAllButtons");
			return;
		}
		writeMessage("disable");
		opponent.play();
	}
	
	private void printWinner() {
		if(isWinner()) {
			endingMessage(name + " is the winner!");
		}
		else {
			endingMessage(opponent.name + " is the winner!");
		}
	}
	
	private boolean isWinner() {
		if (board.xWins() && this.mark == 'X') {
			return true;
		}
		else if (board.oWins() && this.mark == 'O') {
			return true;
		}
		else {
			return false;
		}
	}
	
	private void updateOpponent(String coords, char mark) {
		opponent.writeMessage("updateButtons");
		opponent.writeMessage(coords);
		opponent.writeMessage(Character.toString(mark));
		opponent.writeMessage(EOM);
	}
	
	/**
	 * Allows a player to make a move, choosing from the available cells.
	 * @throws IOException
	 */
	public String makeMove() throws IOException {
		int row = 0;
		int col = 0;
		String in = "";
		while(true) {
			in = readMessage();
			if(!in.isBlank()) {
				row = Integer.parseInt(Character.toString(in.charAt(0)));
				col = Integer.parseInt(Character.toString(in.charAt(1)));
				board.addMark(row, col, mark);
				return in;
			}
		}
	}
	
	/**
	 * Sets the opponent player for this player
	 * @param thePlayer the opponent
	 */
	public void setOpponent(Player thePlayer) {
		this.opponent = thePlayer;
	}
	
	/**
	 * Sets the board being played on
	 * @param theBoard the board being played
	 */
	public void setBoard(Board theBoard) {
		this.board = theBoard;
	}
	
	public String readMessage() {
		String message = "";
		try {
			message = socketIn.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	public void writeMessage(String message) {
		socketOut.println(message);
	}

/////////////////
// HELPER METHODS
	/*
	 * Prints out the winner's name
	 * @param pName the winner's name
	 */
//	private void printWinner(String name) {
	private void endingMessage(String message) {
//		System.out.println("test in printWinner in Player");
		writeMessage(message);
		opponent.writeMessage(message);
	}
	
}
