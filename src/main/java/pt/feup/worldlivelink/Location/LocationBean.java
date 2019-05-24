package pt.feup.worldlivelink.Location;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class LocationBean implements Serializable {

    @NotNull
    private String location;

    private String city;

    private String latitude;

    private String longitude;



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
