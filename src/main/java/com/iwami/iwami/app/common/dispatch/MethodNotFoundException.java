package com.iwami.iwami.app.common.dispatch;

public class MethodNotFoundException extends Exception {

	private static final long serialVersionUID = 9116534853335415345L;

	public MethodNotFoundException() {
        super();
    }

    public MethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotFoundException(String message) {
        super(message);
    }

    public MethodNotFoundException(Throwable cause) {
        super(cause);
    }

}
