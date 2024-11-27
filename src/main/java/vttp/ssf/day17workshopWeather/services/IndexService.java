package vttp.ssf.day17workshopWeather.services;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import vttp.ssf.day17workshopWeather.repositories.CityRepository;

@Service
public class IndexService {

    @Autowired
    private CityRepository cityRepo;

    private Logger logger = Logger.getLogger(IndexService.class.getName());
    private static final String JsonCityFileName = "city.list.json";

    // Get a Sorted Map of <City Name, City ID> from Json List (Provided by https://bulk.openweathermap.org/sample/)
    public SortedMap<String, String> getCityNameAndId()  {

        Optional<SortedMap<String, String>> opt = cityRepo.getCityMap();

        if (!opt.isEmpty()) {
            logger.info(">>>> Obtaining cityMap from Redis");
            return opt.get();
        }

        // if cityMap is not in Redis ("Cache"), then need to read from "static/city.list.json"
        try {

            InputStream is = getClass().getClassLoader().getResourceAsStream("static/" + JsonCityFileName);
            JsonReader reader = Json.createReader(is);
            JsonArray arr = reader.readArray();
            Map<String, String> cityMap = arr.stream()
                    .map(temp -> temp.asJsonObject())
                    .collect(Collectors.toMap(obj -> obj.getString("name"), 
                                              obj -> String.valueOf(obj.getInt("id")),
                                              (existing, replacement) -> existing));

            SortedMap<String, String> cityMapSorted = new TreeMap<>(cityMap);

            // save the city map to Repo
            cityRepo.saveCityMap(cityMapSorted);

            return cityMapSorted;
            
        } catch (Exception e) {
            logger.warning("Error obtaining List of City from Json File");
            e.printStackTrace();
            return new TreeMap<>();
        }
    }
    
}
