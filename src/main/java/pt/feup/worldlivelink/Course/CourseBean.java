package pt.feup.worldlivelink.Course;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class CourseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String id;

    @NotNull
    private String initials;
    @NotNull
    private String type;

    //Constructor without argument
    public CourseBean(){}

    public CourseBean(String id,String name, String initials, String type ){
        this.name = name;
        this.initials = initials;
        this.type = type;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public CourseBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public CourseBean setId(int id) {
        this.id = id;
        return this;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
