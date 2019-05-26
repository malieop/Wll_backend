
package pt.feup.worldlivelink.Alumni;

import org.hibernate.validator.constraints.UniqueElements;
import pt.feup.worldlivelink.Location.LocationBean;

import javax.validation.constraints.NotNull;


//@Component
public class AlumniBean extends AlumniRequestBean {

    @NotNull
    @UniqueElements
    private String id;


    public AlumniBean() {}

    public AlumniBean(String id, String name, LocationBean location) {
        super(name, location);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
