package pt.feup.worldlivelink.Alumni;

import org.springframework.stereotype.Component;
import pt.feup.worldlivelink.Location.LocationBean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


//@Component
public class AlumniBean implements Serializable {

    @NotNull
    @Size(min=2)
    String name;

    LocationBean location;

    @NotNull
    Long id;

    @NotNull
    @Past
    Date birthdate;

    public AlumniBean(Long id, String name, LocationBean location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.birthdate = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
}
