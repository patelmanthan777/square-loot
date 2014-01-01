package event;

public enum KeyState {
	PRESSED, HELD, RELEASED, INACTIVE;
	static public int size = KeyState.values().length;
}
