package encrypt;
public enum DebugLevel {
	All(5), MIDDLE(3), WRAIN(2), ERR(1), NONE(0);

	private int value;

	public int getValue() {
		return value;
	}

	private DebugLevel(int value) {
		this.value = value;
	}

}