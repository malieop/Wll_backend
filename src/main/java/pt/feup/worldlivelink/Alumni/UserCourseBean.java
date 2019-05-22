package pt.feup.worldlivelink.Alumni;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserCourseBean implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private String university;
    @NotNull
    @NotBlank
    private String startdate;

    private String enddate;


    //Constructor without arguments
    public UserCourseBean(){
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}


