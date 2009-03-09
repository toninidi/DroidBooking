package it.booking.exception;

public class AvailableDayNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public AvailableDayNotFoundException(String message) {
		super(message);
	}
	
}
