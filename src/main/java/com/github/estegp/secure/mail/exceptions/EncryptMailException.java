package com.github.estegp.secure.mail.exceptions;

public class EncryptMailException extends Exception{

    public EncryptMailException() {
        super();
    }

    public EncryptMailException(String message) {
        super(message);
    }

    public EncryptMailException(Throwable cause) {
        super(cause);
    }

    public EncryptMailException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncryptMailException(Exception ex) {
        super(ex);
    }

}
