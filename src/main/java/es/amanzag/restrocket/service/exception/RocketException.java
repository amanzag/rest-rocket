package es.amanzag.restrocket.service.exception;

public class RocketException extends Exception {
	private static final long serialVersionUID = -3854333502331757002L;

	public RocketException() {
		super();
	}

	public RocketException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
	}

	public RocketException(String message, Throwable cause) {
		super(message, cause);
	}

	public RocketException(String message) {
		super(message);
	}

	public RocketException(Throwable cause) {
		super(cause);
	}

}
