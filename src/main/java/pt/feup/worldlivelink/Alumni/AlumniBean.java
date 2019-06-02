
package pt.feup.worldlivelink.Alumni;

import org.hibernate.validator.constraints.UniqueElements;
import pt.feup.worldlivelink.Location.LocationBean;

import javax.validation.constraints.NotNull;


//@Component
public class AlumniBean extends AlumniRequestBean {

    @NotNull
    @UniqueElements
    private String id;

    @NotNull
    private String isadmin;

    @NotNull
    private String status;


    public AlumniBean() {}

    public AlumniBean(String id, String name, LocationBean location, String isadmin, String status) {
        super(name, location);
        this.id = id;
        this.isadmin = isadmin;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getStatus() {
        return status;
    }

    public String getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(String isadmin) {
        this.isadmin = isadmin;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
