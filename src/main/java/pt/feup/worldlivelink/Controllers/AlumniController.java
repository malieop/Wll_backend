package pt.feup.worldlivelink.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.feup.worldlivelink.Alumni.AlumniBean;
import pt.feup.worldlivelink.Alumni.AlumniDaoService;
import pt.feup.worldlivelink.Alumni.AlumniRequestBean;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


@RestController
public class AlumniController {

    @Autowired
    private static AlumniDaoService alumniDaoService = new AlumniDaoService();

    @GetMapping("/alumni")
    public Collection<AlumniBean> getListAlumni() {
        return alumniDaoService.getAlumni();
    }

    // only application/json, application/*+json, application/json, application/*+json formats supported
    @PostMapping("/alumni")
    public ResponseEntity<Object> createAlumni(final @Valid @RequestBody AlumniRequestBean alumnus) {
        try {
            alumniDaoService.saveAlumni(alumnus);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("failed to create alumni", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Created alumni", HttpStatus.OK);
    }

    @GetMapping("/alumnibyname/{name}")
    public Collection<AlumniBean> getAlumniByName(@PathVariable String name) {

        return AlumniDaoService.    getAlumniByName(name);
    }

    @GetMapping("/alumnibyid/{id}")
    public AlumniBean getAlumniById(@PathVariable Long id) {
        return alumniDaoService.getAlumniById(id);
    }

    @DeleteMapping("/alumnibyid/{id}")
    public ResponseEntity<Object> deleteAlumniById(@PathVariable Long id) {

        if (alumniDaoService.deleteAlumni(id)) {
            return ResponseEntity.ok().build();
        }
        else {
            return new ResponseEntity("failed to delete alumni", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/alumnibylocation/{location}")
    public Collection<AlumniBean> getAlumniByLocation(@PathVariable String location) {

        return AlumniDaoService.getAlumniByLocation(location);
    }

    @GetMapping("/alumnibycourse/{course}")
    public Collection<AlumniBean> getAlumniByCourse(@PathVariable String course) {

        return AlumniDaoService.getAlumniByCourse(course);
    }
    @GetMapping("/alumnibyyear/{year}")
    public Collection<AlumniBean> getAlumniByYear(@PathVariable String year) {

        return AlumniDaoService.getAlumniByYear(year);
    }

}
