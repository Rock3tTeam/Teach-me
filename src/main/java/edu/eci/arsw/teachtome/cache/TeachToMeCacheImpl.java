package edu.eci.arsw.teachtome.cache;

import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Point;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import edu.eci.arsw.teachtome.services.TeachToMeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Clase que implementa el sistema de cache de la aplicación Open Weather
 */
@Service
public class TeachToMeCacheImpl implements TeachToMeCache {

    private static final long MINUTES_IN_CACHE = 5;
    private final ConcurrentHashMap<Long, Draw> cache = new ConcurrentHashMap<>();

    @Autowired
    private TeachToMeServices teachToMeServices;


    @Override
    public Draw getDrawFromClass(long classId) throws TeachToMeServiceException {
        return cache.get(classId);
    }


    @Override
    public void putDrawOfClassInCache(long classId, Draw draw) throws TeachToMeServiceException {
        if (cache.containsKey(classId)) {
            throw new TeachToMeServiceException("Esa información ya esta en cache");
        }
        cache.put(classId, draw);
    }

    @Override
    public void updateDrawInCache(long classId, Draw draw) throws TeachToMeServiceException {
        List<Point> points = cache.get(classId).getPoints();
        points.addAll(draw.getPoints());
        draw.setDateOfDraw(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    public void cleanDrawOfCache(long classId) throws TeachToMeServiceException {
        cache.remove(classId);
    }

    @Override
    public boolean isDrawInCache(long classId) throws TeachToMeServiceException {
        Draw draw = cache.get(classId);
        boolean isInCache = true;
        if (draw == null) {
            isInCache = false;
        } else if (LocalDateTime.now().isAfter(draw.getDateOfDraw().toLocalDateTime().plusMinutes(MINUTES_IN_CACHE))) {
            cleanDrawOfCache(classId);
            isInCache = false;
        }
        return isInCache;
    }
}