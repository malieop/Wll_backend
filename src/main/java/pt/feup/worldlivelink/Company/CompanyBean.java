package pt.feup.worldlivelink.Company;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CompanyBean implements Serializable {

    @NotNull
    private String name;

    private String email;


    public CompanyBean(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
