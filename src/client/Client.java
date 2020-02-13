package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
	
	private Socket aSocket;
	private PrintWriter socketOut;
	private BufferedReader socketIn;
	private BufferedReader stdIn;
	private ClientController client;
	
	private final String EOM = "EOM"; // end of message terminating string

	
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
	
	public void TEST(String message) {
		System.out.println(message);
	}
	
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
	
	private String receiveMessage() {
		String response = "";
		try {
			response = socketIn.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
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
	
	private void closeSockets() {

		try {
			stdIn.close();
			socketIn.close();
			socketOut.close();
		}catch (IOException e) {
			e.getStackTrace();
		}
	}
	
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

	
	public static void main(String[] args) throws IOException{
		String gameHeading = "tic-tac-toe", initialName = "";
		int gameWidth = 240, gameHeight = 340;
		
		ClientPlayer player = new ClientPlayer(initialName, '-');
		ClientView view = new ClientView(gameHeading, gameWidth, gameHeight);
		ClientController clientController = new ClientController(view, player, null, null);
		clientController.setAllButtonsEnabled(false);
		Client client = new Client ("localhost", 9090, clientController);
		client.communicate();
	}

}
