package vttp.ssf.day17workshopWeather.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import vttp.ssf.day17workshopWeather.models.WeatherReport;
import vttp.ssf.day17workshopWeather.services.WeatherService;

import static vttp.ssf.day17workshopWeather.Constants.*;

import java.util.SortedMap;

@Controller
@RequestMapping
public class WeatherController {

    @Autowired
    private WeatherService weatherSvc;

    @GetMapping("/weather")
    public ModelAndView getWeather(
        @RequestParam(name = ATTR_CITY_NAME) String cityName,
        @RequestParam(name = ATTR_UNITS) String units,
        HttpSession session) {

        SortedMap<String, String> citySortedMap = getCityMapFromSession(session);

        WeatherReport report = weatherSvc.getWeatherReport(citySortedMap.get(cityName), units);
        
        ModelAndView mav = new ModelAndView("weather");
        mav.addObject(ATTR_CITY_NAME, cityName);
        mav.addObject(ATTR_WEATHER_REPORT, report);

        return mav;
    }

}
