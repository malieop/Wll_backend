package pt.feup.worldlivelink.Course;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class CourseBean implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private String id;

    @NotNull
    private String initals;

    @NotNull
    private String type;

    //Constructor without argument
    public CourseBean(){}

    public CourseBean(String id,String name, String initals, String type ){
        this.name = name;
        this.id = id;
        this.initals = initals;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInitals() {
        return initals;
    }

    public void setInitals(String id) {
        this.initals = initals;
    }

    public String getType() {
        return type;
    }

    public void setType(String id) {
        this.type = type;
    }

}
