package client;

public class ClientPlayer {
	
	private String name;
	private char mark;
	
	public ClientPlayer(String name, char mark) {
		this.setName(name);
		this.setMark(mark);
	}

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
