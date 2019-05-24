package pt.feup.worldlivelink.Company;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CompanyBean implements Serializable {

    @NotNull
    private String name;

    private String email;

    private String job;

    private String startDate;



    public CompanyBean(){}

    public String getName() {
        return name;
    }

    public CompanyBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CompanyBean setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getJob() {
        return job;
    }

    public CompanyBean setJob(final String job) {
        this.job = job;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public CompanyBean setStartDate(final String startDate) {
        this.startDate = startDate;
        return this;
    }
}
