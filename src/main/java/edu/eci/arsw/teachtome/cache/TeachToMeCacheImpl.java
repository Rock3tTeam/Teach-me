package edu.eci.arsw.teachtome.cache;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.persistence.TeachToMePersistence;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachToMeCacheImpl implements TeachToMeCache {

    @Autowired
    private TeachToMePersistence persistence;

    @Cacheable(key= "#nameFilter" , cacheNames= "filtered-classes-cache" , unless="#result == null")
    @Override
    public List<Clase> getFilteredClassesFromCache(String nameFilter) throws TeachToMeServiceException {
        return persistence.getFilteredClassesByName(nameFilter);
    }
}
