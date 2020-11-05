package edu.eci.arsw.teachtome.cache;

import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import org.springframework.stereotype.Service;

/**
 * Interfaz para servicio de cache de la aplicación Open Weather
 */
@Service
public interface TeachToMeCache {

    /**
     * Metodo que obtiene el dibujo de una clase guardado en cache
     * @param classId la clase sobre la cual se va a obtener el dibujo
     * @return el dibujo de la clase guardado en cache
     * @throws TeachToMeServiceException si la clase no existe
     */
    Draw getDrawFromClass(long classId) throws TeachToMeServiceException;

    /**
     * Metodo que añade el dibujo de una clase  en cache
     * @param classId la clase sobre la cual se va a guardar el dibujo
     * @param draw el dibujo a guardar en cache
     * @throws TeachToMeServiceException si la clase no existe
     */
    void putDrawOfClassInCache(long classId, Draw draw) throws TeachToMeServiceException;

    /**
     * Actualiza el valor de un dibujo en cache
     * @param classId La clase del dibujo guardado en cache
     * @param draw el dibujo a ser guardado en cache
     * @throws TeachToMeServiceException si la clase no existe
     */
    void updateDrawInCache(long classId, Draw draw) throws TeachToMeServiceException;

    /**
     * Elimina un dibujo de una clase del cache
     * @param classId la clase sobre la cual se va a eliminar el dibujo
     * @throws TeachToMeServiceException si la clase no existe
     */
    void cleanDrawOfCache(long classId) throws TeachToMeServiceException;

    /**
     * Determina si un dibujo de una clase existe en cache
     * @param classId La clase sobre la cual se va a buscar el dibujo en cache
     * @return si un dibujo de una clase existe en cache
     * @throws TeachToMeServiceException si la clase no existe
     */
    boolean isDrawInCache(long classId) throws TeachToMeServiceException;
}