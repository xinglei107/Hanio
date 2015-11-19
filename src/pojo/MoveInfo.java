package pojo;

public class MoveInfo {

	private int number;
	private int from;
	private int to;

	public MoveInfo(int number, int from, int to) {
		this.number = number;
		this.from = from;
		this.to = to;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}
}
