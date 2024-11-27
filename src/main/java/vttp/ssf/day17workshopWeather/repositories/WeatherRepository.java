package vttp.ssf.day17workshopWeather.repositories;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.ssf.day17workshopWeather.models.WeatherReport;

@Repository
public class WeatherRepository {

    @Autowired @Qualifier("redis-string")
    private RedisTemplate<String, String> template;

    // set id_unit temperature ... / set id_unit iconUrl ... / set id_unit description ...
    // expire id_unit 180
    public void saveWeatherReport(WeatherReport weatherReport) {

        HashOperations<String, String, String> hashOps = template.opsForHash();

        String identifier = "%s_%s".formatted(weatherReport.getCityId(), weatherReport.getUnits());

        hashOps.put(identifier, "description", weatherReport.getDescription());
        hashOps.put(identifier, "iconUrl", weatherReport.getIconUrl());
        hashOps.put(identifier, "temperature", String.valueOf(weatherReport.getTemperature()));

        // Set the key to expire in 300 seconds
        template.expire(identifier, Duration.ofSeconds(300));
    }

    // get weather information, if don't exist, return Optional<Empty>
    // hgetall id
    public Optional<WeatherReport> getWeatherReportById(String id, String units) {

        String identifier = "%s_%s".formatted(id, units);

        if (!template.hasKey(identifier))
            return Optional.empty();

        HashOperations<String, String, String> hashOps = template.opsForHash();

        WeatherReport weatherReport = new WeatherReport();

        weatherReport.setCityId(identifier);
        weatherReport.setDescription(hashOps.get(identifier, "description"));
        weatherReport.setIconUrl(hashOps.get(identifier, "iconUrl"));
        weatherReport.setTemperature(Integer.parseInt(hashOps.get(identifier, "temperature")));

        return Optional.of(weatherReport);
    }
    
}
