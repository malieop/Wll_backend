
package pt.feup.worldlivelink.Alumni;

import pt.feup.worldlivelink.Company.CompanyBean;
import pt.feup.worldlivelink.Location.LocationBean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


//@Component
public class AlumniRequestBean implements Serializable {

    @NotNull
    @Size(min=2)
    private String name;

    @NotNull
    private LocationBean location;

    private CompanyBean company;

    //@NotNull
    //@NotBlank
    @Size(min=2)
    private String username;

    //@NotNull
    //@NotBlank
    @Size(min=2)
    private String password;


    // TODO: ?? missing in mongo
    //@NotNull
    //@NotBlank
    private String birthdate;

    //@NotNull
    private UserCourseBean course;

    //@NotNull
    //@NotBlank
    private String email;

    public AlumniRequestBean() {}


    public AlumniRequestBean(String name, LocationBean location){
        this.name = name;
        this.location = location;
    }

    public AlumniRequestBean(String name, LocationBean location, UserCourseBean userCourse, String birthdate, CompanyBean company ) {
        this.name = name;
        this.location = location;
        this.birthdate = birthdate;
        this.course = userCourse;
        this.company = company;
    }

    public AlumniRequestBean(String username, LocationBean location,UserCourseBean userCourse, CompanyBean company){
        this.username = username;
        this.location = location;
        this.course = userCourse;
        this.company = company;

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

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
