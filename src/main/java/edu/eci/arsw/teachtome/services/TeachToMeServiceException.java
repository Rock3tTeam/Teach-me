package edu.eci.arsw.teachtome.services;

/**
 * Excepción personalizada de errores causados en la capa de servicios
 */
public class TeachToMeServiceException extends Exception {
    public static final String NULL_EMAIL = "El email no puede ser nulo";
    public static final String BAD_FORMAT = "JSON Bad Format";

    /**
     * Constructor de la clase TeachToMeServiceException
     *
     * @param msg - Mensaje de la excepción
     */
    public TeachToMeServiceException(String msg) {
        super(msg);
    }

    /**
     * Constructor de la clase TeachToMeServiceException
     *
     * @param message - Mensaje de la excepción
     * @param cause   - Causa de la excepción
     */
    public TeachToMeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
