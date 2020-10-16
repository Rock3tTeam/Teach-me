package edu.eci.arsw.teachtome.persistence;

/**
 * Excepción personalizada de errores causados en la capa de persistencia
 */
public class TeachToMePersistenceException extends Exception {
    public static final String NON_EXISTING_CLASS = "No existe la clase con el id " ;
    /**
     * Constructor de la clase TeachToMePersistenceException
     *
     * @param msg - Mensaje de la excepción
     */
    public TeachToMePersistenceException(String msg) {
        super(msg);
    }

    /**
     * Constructor de la clase TeachToMePersistenceException
     *
     * @param message - Mensaje de la excepción
     * @param cause   - Causa de la excepción
     */
    public TeachToMePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
