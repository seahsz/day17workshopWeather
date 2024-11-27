package vttp.ssf.day17workshopWeather;

import java.util.SortedMap;
import java.util.TreeMap;

import jakarta.servlet.http.HttpSession;

public class Constants {

    public static final String ATTR_CITY_NAME = "cityName";
    public static final String ATTR_CITY_MAP = "cityMap";

    public static final String ATTR_UNITS = "units";

    public static final String ATTR_ID = "id";
    public static final String ATTR_WEATHER_REPORT = "weatherReport";

    @SuppressWarnings("unchecked")
    public static SortedMap<String, String> getCityMapFromSession(HttpSession session) {

        Object cityMapObject = session.getAttribute(ATTR_CITY_MAP);

        if (cityMapObject instanceof SortedMap) {
            return (SortedMap<String, String>) cityMapObject;
        } else {
            return new TreeMap<>();
        }
    }
    
}
