package edu.eci.arsw.teachtome.services;

public class TeachToMeServiceException extends Exception {
    public TeachToMeServiceException(String msg) {
        super(msg);
    }

    public TeachToMeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
