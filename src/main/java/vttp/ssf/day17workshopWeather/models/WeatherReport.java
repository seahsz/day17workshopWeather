package vttp.ssf.day17workshopWeather.models;

public class WeatherReport {

    private String cityId;
    private String units;
    private String description;
    private String iconUrl;
    private int temperature;

    public String getCityId() { return cityId; }
    public void setCityId(String cityId) { this.cityId = cityId; } 

    public String getUnits() { return units; }
    public void setUnits(String units) { this.units = units; }   

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public int getTemperature() { return temperature; }
    public void setTemperature(int temperature) { this.temperature = temperature; }
    
}
