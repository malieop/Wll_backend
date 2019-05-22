
package pt.feup.worldlivelink.Alumni;
import org.springframework.stereotype.Component;
import pt.feup.worldlivelink.Company.CompanyBean;
import pt.feup.worldlivelink.Location.LocationBean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


//@Component
public class AlumniBean extends AlumniRequestBean {

    @NotNull
    private String id;


    public AlumniBean(String id, String name, LocationBean location) {
        super(name, location);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
