package pt.feup.worldlivelink.Alumni;

import pt.feup.worldlivelink.Company.CompanyBean;
import pt.feup.worldlivelink.Location.LocationBean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class AlumniUpdateInformationBean implements Serializable {

    private LocationBean location;
    private UserCourseBean course;
    private CompanyBean company;

    @NotNull
    @NotBlank
    @Size(min=2)
    public String username;

    public AlumniUpdateInformationBean(LocationBean location, UserCourseBean course, CompanyBean company, @NotNull @NotBlank @Size(min = 2) String username) {
        this.location = location;
        this.course = course;
        this.company = company;
        this.username = username;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public UserCourseBean getCourse() {
        return course;
    }

    public void setCourse(UserCourseBean course) {
        this.course = course;
    }

    public CompanyBean getCompany() {
        return company;
    }

    public void setCompany(CompanyBean company) {
        this.company = company;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
