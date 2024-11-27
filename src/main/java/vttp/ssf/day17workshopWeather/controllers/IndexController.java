package vttp.ssf.day17workshopWeather.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import vttp.ssf.day17workshopWeather.services.IndexService;

import static vttp.ssf.day17workshopWeather.Constants.*;

import java.util.SortedMap;

@Controller
@RequestMapping
public class IndexController {

    @Autowired
    private IndexService indexSvc;

    @GetMapping(path={"/", "index.html"})
    public ModelAndView getIndex(HttpSession session) {

        ModelAndView mav = new ModelAndView("index");

        // Get SortedMap <City Name, Int id>
        SortedMap<String, String> citySortedMap = indexSvc.getCityNameAndId();
        session.setAttribute(ATTR_CITY_MAP, citySortedMap);

        mav.addObject(ATTR_CITY_MAP, citySortedMap);

        return mav;
    }
    
}
