package vttp.ssf.day17workshopWeather.services;

import java.io.StringReader;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.ssf.day17workshopWeather.models.WeatherReport;
import vttp.ssf.day17workshopWeather.repositories.WeatherRepository;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepo;

    @Value("${spring.data.weather.apikey}")
    private String OPEN_WEATHER_API_KEY;

    private static final String OPEN_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    private static final String OPEN_WEATHER_ICON_BASE_URL = "https://openweathermap.org/img/wn/";

    private Logger logger = Logger.getLogger(WeatherService.class.getName());

    // Obtain weather from openweathermap.org API
    // Returns WeatherReport object (members: description, icon) - null if no result found
    public WeatherReport getWeatherReport(String id, String units) {

        // Get WeatherReport from Redis database ("cache") if available
        Optional<WeatherReport> opt = weatherRepo.getWeatherReportById(id, units);

        // if empty, get from API
        if (opt.isEmpty())
            return getWeatherReportFromApi(id, units);

        else {
            logger.info(">>> Got Weather Report from Redis");
            return opt.get();
        }
        
    }

    // Obtain weather from openweathermap.org API
    // Returns WeatherReport object (members: description, icon, temperature, id, unit)
    public WeatherReport getWeatherReportFromApi(String id, String units) {

        // Generate the URL
        String searchUrl = UriComponentsBuilder
                .fromUriString(OPEN_WEATHER_URL)
                .queryParam("appid", OPEN_WEATHER_API_KEY)
                .queryParam("id", id)
                .queryParam("units", units)
                .toUriString();
        
        System.out.println(">>>>> Search URL is: " + searchUrl);

        // Configure the request
        RequestEntity<Void> request = RequestEntity
                .get(searchUrl)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        // Create REST template
        RestTemplate template = new RestTemplate();

        // Get the Response
        ResponseEntity<String> response;

        try {

            response = template.exchange(request, String.class);

            // Extract the payload
            String payload = response.getBody();
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject result = reader.readObject();

            JsonArray weather = result.getJsonArray("weather");

            /* Just get the first one if weather is not empty (in case there are multiple
            * readings from multiple weather stations)
            */

            // Get the "description" and "icon"
            WeatherReport weatherReport = new WeatherReport();

            if (! weather.isEmpty()) {
                JsonObject obj = weather.getJsonObject(0);
                weatherReport.setDescription(obj.getString("description"));

                // https://openweathermap.org/weather-conditions shows how to generate img url from "icon"
                // https://openweathermap.org/img/wn/(ICON)@2x.png e.g. https://openweathermap.org/img/wn/10d@2x.png
                String iconUrl = OPEN_WEATHER_ICON_BASE_URL + obj.getString("icon") + "@2x.png";

                weatherReport.setIconUrl(iconUrl);

                int temperature = result.getJsonObject("main").getInt("temp");
                weatherReport.setTemperature(temperature);

                // set the "id" and "units" into the weatherReport
                weatherReport.setCityId(id);
                weatherReport.setUnits(units);

                // save the WeatherReport into Redis as "cache"
                weatherRepo.saveWeatherReport(weatherReport);
            }

            return weatherReport;

        } catch (Exception e) {
            e.printStackTrace();
            return new WeatherReport();
        }
    }
    
}
