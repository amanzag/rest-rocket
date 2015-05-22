package es.amanzag.restrocket.dto;

public class Position {
	private int value;
	
	private Direction direction;
	
	private int pitch; // http://en.wikipedia.org/wiki/Aircraft_principal_axes

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getPitch() {
		return pitch;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}
	
	
	
}
