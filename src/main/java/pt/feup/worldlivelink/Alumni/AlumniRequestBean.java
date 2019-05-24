
package pt.feup.worldlivelink.Alumni;
import org.springframework.stereotype.Component;
import pt.feup.worldlivelink.Company.CompanyBean;
import pt.feup.worldlivelink.Location.LocationBean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


//@Component
public class AlumniRequestBean implements Serializable {

    @NotNull
    @Size(min=2)
    String name;

    @NotNull
    LocationBean location;

    CompanyBean company;


    // TODO: ?? missing in mongo
    @NotNull
    @NotBlank
    String birthdate;

    @NotNull
    UserCourseBean course;

    @NotNull
    @NotBlank
    String email;

    public AlumniRequestBean() {

    }


    public AlumniRequestBean(String name, LocationBean location){
        this.name = name;
        this.location = location;
    }

    public AlumniRequestBean(String name, LocationBean location, UserCourseBean userCourse, String birthdate, CompanyBean company ) {
        this.name = name;
        this.location = location;
        this.birthdate = birthdate;
        this.course=userCourse;
        this.company=company;
    }

    public CompanyBean getCompany() {
        return company;
    }

    public void setCompany(CompanyBean company) {
        this.company = company;
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public UserCourseBean getCourse() {
        return course;
    }

    public void setCourse(UserCourseBean course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
