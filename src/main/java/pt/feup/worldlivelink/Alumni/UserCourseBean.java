package pt.feup.worldlivelink.Alumni;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserCourseBean implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private String university;

    @NotNull
    @NotBlank
    private String startDate;

    private String endDate;


    //Constructor without arguments
    public UserCourseBean(){
    }

    public String getUniversity() {
        return university;
    }

    public UserCourseBean setUniversity(String university) {
        this.university = university;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public UserCourseBean setStartDate(final String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public UserCourseBean setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserCourseBean setName(String name) {
        this.name = name;
        return this;
    }

}


