package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Client app for the online tic tac toe game
 * @author Sean Barton
 *
 */
public class Client {
	
	private Socket aSocket;
	private PrintWriter socketOut;
	private BufferedReader socketIn;
	private BufferedReader stdIn;
	private ClientController client;
	
	private final String EOM = "EOM"; // end of message terminating string

	/**
	 * Constructs the client for online gaming
	 * @param serverName - The server to connect to
	 * @param portNumber - The server TCP port
	 * @param client - the client view controller
	 */
	public Client (String serverName, int portNumber, ClientController client) {
		
		try {
			aSocket = new Socket (serverName, portNumber);
			// socket input stream
			socketIn = new BufferedReader (new InputStreamReader (aSocket.getInputStream()));
			// socket output stream
			socketOut = new PrintWriter((aSocket.getOutputStream()), true);
			// keyboard input stream
			stdIn = new BufferedReader (new InputStreamReader (System.in));
			
			this.client = client;
			client.setSocketIn(socketIn);
			client.setSocketOut(socketOut);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Establishes the communication to the server. This is a receiver message switchboard
	 */
	public void communicate () {
		String line = "";
		
		System.out.println("Connecting to server...");
		
		// receive all messages from server
		line = receiveMessage();
		while(true) {
			line = receiveMessage();
			if(line.equals(EOM)) {
				break;
			}
			else if(line.equals("setMark")) {
				setViewMark();
				continue;
			}
			else if(line.equals("getNames")) {
				getPlayerName();
				continue;
			}
			else if(line.equals("getRowCol")) {
				getRowOrColumn();
				continue;
			}
			else if(line.equals("enableAllButtons")) {
				client.setAllButtonsEnabled(true);
				continue;
			}
			else if(line.equals("disableAllButtons")) {
				client.setAllButtonsEnabled(false);
				continue;
			}
			else if(line.equals("disable")) {
				client.setEnabled(false);
				continue;
			}
			else if(line.equals("enable")) {
				client.setEnabled(true);
				continue;
			}
			else if(line.equals("updateButtons")) {
				setButtonMark();
				continue;
			}
			client.setViewMessage(line);
		}
		
		closeSockets();
	}
	
	/**
	 * Sets the button as marked with the opponent's mark.
	 */
	private void setButtonMark() {
		String response = "";
		String coords = "";
		String markString = "";
		ArrayList<String> list = new ArrayList<String>();
		int row, col;
		char mark;
		
		while(true) {
			response = receiveMessage(); // read responses from the server
			if(response.equals(EOM)) {
				coords = list.get(0);
				markString = list.get(1);
				row = Integer.parseInt(Character.toString(coords.charAt(0)));
				col = Integer.parseInt(Character.toString(coords.charAt(1)));
				mark = markString.charAt(0);
				client.setButtonMark(row, col, mark);
				client.setButtonDisabled(row, col);
				break;
			}
			list.add(response);
		}	
	}
	
	/**
	 * Sets the UI mark, either X or O
	 */
	private void setViewMark() {
		String response = "";
		
		while(true) {
			response = receiveMessage();
			if(response.equals(EOM)) {
				break;
			}
			else if (!response.isBlank()) {
				char mark = response.charAt(0);
				client.setViewMark(mark);
				client.setPlayerMark(mark);
			}
		}
	}
	
	/**
	 * Receives a TCP message from the server
	 * @return
	 */
	private String receiveMessage() {
		String response = "";
		try {
			response = socketIn.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * Gets the player name from the server
	 */
	private void getPlayerName() {
		String line = "";
		String response = "";
		
		while(true) {
			response = receiveMessage(); // read responses from the server
			if(response.equals(EOM)) {
				break;
			}
			client.setViewMessage(response);
			line = client.getPlayerName();//getStdInput(); // get the user name
			socketOut.println(line); // send back to server
		}
	}
	
	/**
	 * Closes all client sockets
	 */
	private void closeSockets() {

		try {
			stdIn.close();
			socketIn.close();
			socketOut.close();
		}catch (IOException e) {
			e.getStackTrace();
		}
	}
	
	/**
	 * Gets the players marked row or column from the server
	 */
	private void getRowOrColumn() {
		
		String response = "";
		String line = "";
		
		while(true) {
			
			try {
				response = receiveMessage();
				if(response.equals(EOM)) {
					break;
				}
				System.out.println(response);
				line = stdIn.readLine(); // read a line from the keyboard
				socketOut.println(line); // send back to server
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initiates the game
	 * @param args - Takes the sever host name
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		String gameHeading = "tic-tac-toe", initialName = "";
		int gameWidth = 240, gameHeight = 340;
		
		ClientPlayer player = new ClientPlayer(initialName, '-');
		ClientView view = new ClientView(gameHeading, gameWidth, gameHeight);
		ClientController clientController = new ClientController(view, player, null, null);
		clientController.setAllButtonsEnabled(false);
		// TODO make the following to take the host name, not localhost
		Client client = new Client ("localhost", 9090, clientController);
		client.communicate();
	}

}
