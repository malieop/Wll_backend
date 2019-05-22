package pt.feup.worldlivelink.Company;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CompanyBean implements Serializable {

    @NotNull
    private String name;

    private String email;

    private String job;

    private String startdate;



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
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }
}
