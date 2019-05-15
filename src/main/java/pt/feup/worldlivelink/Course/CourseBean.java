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

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
