package edu.eci.arsw.teachtome.Cache;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface TeachToMeCache {

    /**
     * Devuelve las clases en base a un filtro de una palabra
     * @param nameFilter palabra clave por la cual se van a filtrar las clases
     * @return las clases que correspondan al filtro
     */
    @Cacheable(key= "#nameFilter" , value = "filtered-classes-cache" , unless="#result == null")
    List<Clase>  getFilteredClassesFromCache(String nameFilter) throws TeachToMeServiceException;
}
