package client;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * The main GUI for the game
 * @author Sean Barton
 *
 */
public class ClientView extends JFrame {
	
	private JButton[][] gridButton;
	private JTextField nameInput;
	private JTextArea messageOutput, markOutput;
	private JPanel buttonPanel;
	private static final int grid = 3;
	private int nameInputWidth = 8;
	
	/**
	 * Constructs the game GUI
	 * @param name GUI window name
	 * @param width window width
	 * @param height window height
	 */
	public ClientView (String name, int width, int height) {
		super(name);
		setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		makeMessage();
		makeButtonGrid();
		makePlayerInfo();
		makeVisible(width, height);
	}
	
	/**
	 * Sets the GUI message output 
	 * @param message text to display in the message area
	 */
	public void setMessageOutput(String message) {
		messageOutput.setText(message);
	}
	
	/**
	 * Sets the mark on the GUI to indicate the player affiliation
	 * @param mark the player's mark, X or O
	 */
	public void setMarkOutput(char mark) {
		markOutput.setText(String.valueOf(mark));
	}
	
	/**
	 * Gets the name input into the GUI
	 * @return the name of the player
	 */
	public String getNameInput() {
		return nameInput.getText();
	}
	
	/**
	 * Makes the name field non-editable once it is set.
	 */
	public void setNameInputNonEditable() {
		nameInput.setEditable(false);
	}
	
	/**
	 * Disables a button on the GUI
	 * @param row the button row
	 * @param col the button column
	 */
	public void setButtonDisabled(int row, int col) {
		gridButton[row][col].setEnabled(false);
	}
	
	/**
	 * Sets the player's mark on the button
	 * @param row the button row
	 * @param col the button column
	 * @param mark the player's mark, X or O
	 */
	public void setButtonMark(int row, int col, char mark) {
		gridButton[row][col].setText(String.valueOf(mark));
	}
	
	/**
	 * Enables or disables all the GUI buttons from being pushed.
	 * @param value true to enable, false otherwise
	 */
	public void setButtonsEnabled(boolean value) {
		for (int i = 0; i < grid; i++) {
			for (int j = 0; j < grid; j++) {
				gridButton[i][j].setEnabled(value);
			}
		}	
	}
	
	// *** BUTTON LISTENERS ***
	void addButton00Listener(ActionListener button00lisenter) {
		gridButton[0][0].addActionListener(button00lisenter);
	}
	
	void addButton01Listener(ActionListener button01lisenter) {
		gridButton[0][1].addActionListener(button01lisenter);
	}
	
	void addButton02Listener(ActionListener button02lisenter) {
		gridButton[0][2].addActionListener(button02lisenter);
	}
	
	void addButton10Listener(ActionListener button10lisenter) {
		gridButton[1][0].addActionListener(button10lisenter);
	}

	void addButton11Listener(ActionListener button11lisenter) {
		gridButton[1][1].addActionListener(button11lisenter);
	}

	void addButton12Listener(ActionListener button12lisenter) {
		gridButton[1][2].addActionListener(button12lisenter);
	}

	void addButton20Listener(ActionListener button20lisenter) {
		gridButton[2][0].addActionListener(button20lisenter);
	}

	void addButton21Listener(ActionListener button21lisenter) {
		gridButton[2][1].addActionListener(button21lisenter);
	}

	void addButton22Listener(ActionListener button22lisenter) {
		gridButton[2][2].addActionListener(button22lisenter);
	}
	
	void addTextFieldListener(ActionListener textFieldListener) {
		nameInput.addActionListener(textFieldListener);
	}
	
	// *** HELPER METHODS ***
	private void makeMessage() {
		JLabel messageLabel = new JLabel("Messages");
		messageLabel.setAlignmentX(CENTER_ALIGNMENT);
		messageOutput = new JTextArea();
		messageOutput.setAlignmentX(CENTER_ALIGNMENT);
		messageOutput.setEditable(false);
		messageOutput.setWrapStyleWord(true);
		messageOutput.setLineWrap(true);
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messagePanel.add(messageLabel);
		messagePanel.add(messageOutput);
		messagePanel.setAlignmentX(CENTER_ALIGNMENT);
		add(messagePanel, BorderLayout.NORTH);
	}
	
	private void makeButtonGrid() {
		gridButton = new JButton[grid][grid];
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(grid, grid));
		for (int i = 0; i < grid; i++) {
			for (int j = 0; j < grid; j++) {
				buttonPanel.add(gridButton[i][j] = new JButton());
			}
		}
		add(buttonPanel, BorderLayout.CENTER);
	}
	
	private void makePlayerInfo() {
		JLabel playerMark = new JLabel("Player:");
		markOutput = new JTextArea();
		markOutput.setEditable(false);
		JPanel playerMarkPanel = new JPanel();
		playerMarkPanel.setAlignmentX(CENTER_ALIGNMENT);
		playerMarkPanel.add(playerMark);
		playerMarkPanel.add(markOutput);
		JLabel playerNameLabel = new JLabel("Player Name:");
		nameInput = new JTextField(nameInputWidth);
		JPanel playerNamePanel = new JPanel();
		playerNamePanel.setAlignmentX(CENTER_ALIGNMENT);
		playerNamePanel.add(playerNameLabel);
		playerNamePanel.add(nameInput);
		JPanel playerInfo = new JPanel();
		playerInfo.setLayout(new BoxLayout(playerInfo, BoxLayout.Y_AXIS));
		playerInfo.setAlignmentX(CENTER_ALIGNMENT);
		playerInfo.add(playerMarkPanel);
		playerInfo.add(playerNamePanel);
		add(playerInfo, BorderLayout.SOUTH);
	}
	
	private void makeVisible(int width, int height) {
		pack();
		JPanel voidPanelEast = new JPanel();
		add(voidPanelEast, BorderLayout.EAST);
		JPanel voidPanelWest = new JPanel();
		add(voidPanelWest, BorderLayout.WEST);
		setSize(width, height);
		setVisible(true);
	}
}
