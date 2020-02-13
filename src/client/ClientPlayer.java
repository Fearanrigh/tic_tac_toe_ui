package client;

/**
 * The client side player model
 * @author sean
 *
 */
public class ClientPlayer {
	
	private String name;
	private char mark;
	
	/**
	 * Constructs a player on the client side
	 * @param name - The name of the player
	 * @param mark - The X or O mark of the player
	 */
	public ClientPlayer(String name, char mark) {
		this.setName(name);
		this.setMark(mark);
	}

	// GETTERS AND SETTERS
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getMark() {
		return mark;
	}

	public void setMark(char mark) {
		this.mark = mark;
	}

}
