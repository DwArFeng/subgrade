package com.dwarfeng.subgrade.stack.exception;

public class EventException extends Exception {

    private static final long serialVersionUID = -2245102465402893140L;

    public EventException() {
    }

    public EventException(String message) {
        super(message);
    }

    public EventException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventException(Throwable cause) {
        super(cause);
    }
}
