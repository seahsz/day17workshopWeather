package vttp.ssf.day17workshopWeather.repositories;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CityRepository {

    @Autowired
    @Qualifier("redis-string")
    private RedisTemplate<String, String> template;

    // hset cityMap <String cityname, String id>
    public void saveCityMap(SortedMap<String, String> cityMap) {

        HashOperations<String, String, String> hashOps = template.opsForHash();

        hashOps.putAll("cityMap", cityMap);
        template.expire("cityMap", Duration.ofMinutes(10));
    }

    // hgetall cityMap
    public Optional<SortedMap<String, String>> getCityMap() {

        HashOperations<String, String, String> hashOps = template.opsForHash();

        Map<String, String> entries = hashOps.entries("cityMap");

        // Return empty
        if (entries.isEmpty())
            return Optional.empty();
        
        return Optional.of(new TreeMap<>(entries));
    }

}
