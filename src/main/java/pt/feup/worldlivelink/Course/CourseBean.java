package pt.feup.worldlivelink.Course;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class CourseBean implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private int id;

    //Constructor without arguments
    public CourseBean(){
    }

    public String getName() {
        return name;
    }

    public CourseBean setName(String name) {
        this.name = name;
        return this;
    }

    public int getId() {
        return id;
    }

    public CourseBean setId(int id) {
        this.id = id;
        return this;
    }
}
