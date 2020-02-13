package server;

import java.io.*;

//STUDENTS SHOULD ADD CLASS COMMENTS, METHOD COMMENTS, FIELD COMMENTS 

/**
 * The main tic-tac-toe game class
 * @author ENSF593/594
 *
 */
public class Game implements Constants, Runnable {

	private Board theBoard;
	private Referee theRef;
	private BufferedReader socketIn1, socketIn2;
	private PrintWriter socketOut1, socketOut2;
	
	private final String EOM = "EOM"; // end of message terminator string
	
	/**
	 * Constructs the game with a new board
	 */
    public Game(BufferedReader socketIn1, BufferedReader socketIn2,
    		PrintWriter socketOut1, PrintWriter socketOut2) {
        theBoard  = new Board();
        this.socketIn1 = socketIn1;
        this.socketIn2 = socketIn2;
        this.socketOut1 = socketOut1;
        this.socketOut2 = socketOut2;
	}
    
    /**
     * Appoints a referee for the game
     * @param r the referee
     * @throws IOException bad input from the player gets passed upwards
     */
    public void appointReferee(Referee r) throws IOException {
        theRef = r;
    	theRef.runTheGame();
    }
    
    public void TEST(String message) {
    	System.out.println(message);
    }
    
    private void setMark(PrintWriter socketOut, char mark) {
		socketOut.println("setMark");
		String letterMark = Character.toString(mark);
		socketOut.println(letterMark);
		socketOut.println(EOM);
    }
	
    private String getName(PrintWriter socketOut1, PrintWriter socketOut2, 
    		BufferedReader socketIn1, char mark) {
		socketOut1.println("getNames");
		socketOut1.println("Please enter name");
		socketOut2.println("Waiting for the \'" + mark + "\' player...");
		String name = null;
		while(true) {
			try {
				name = socketIn1.readLine();
				if(name.isBlank()) {
					socketOut1.println("Please enter a name below");
				}
				else {
					socketOut1.println(EOM);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return name;
    }
    
    /**
     * Main entry for the program
     * @param args
     * @throws IOException
     */
	@Override
	public void run() {
		
		Referee theRef;
		Player xPlayer, oPlayer;

		setMark(socketOut1, LETTER_X);
		setMark(socketOut2, LETTER_O);
		
		// getting the name of player 1
		String name = "";
		name = getName(socketOut1, socketOut2, socketIn1, LETTER_X);

		xPlayer = new Player(name, LETTER_X, socketIn1, socketOut1);
		xPlayer.setBoard(theBoard);
		
		
		// Getting the name of player 2
		name = getName(socketOut2, socketOut1, socketIn2, LETTER_O);
		
		oPlayer = new Player(name, LETTER_O, socketIn2, socketOut2);
		oPlayer.setBoard(theBoard);
		
		socketOut1.println("enableAllButtons");
		socketOut2.println("enableAllButtons");
		socketOut2.println("disable");
		
		// Assigning the referee
		theRef = new Referee();
		theRef.setBoard(theBoard);
		theRef.setoPlayer(oPlayer);
		theRef.setxPlayer(xPlayer);
        
        try {
			appointReferee(theRef);
		} catch (IOException e) {
			e.printStackTrace();
		}
        socketOut1.println(EOM);
        socketOut2.println(EOM);
	}
}
