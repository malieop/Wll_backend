package pt.feup.worldlivelink.Controllers;

import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.feup.worldlivelink.Alumni.AlumniBean;
import pt.feup.worldlivelink.Alumni.AlumniDaoService;

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
    public ResponseEntity<Resource<AlumniBean>> createAlumni(final @Valid @RequestBody AlumniBean alumnus) {
        try {
            AlumniBean savedAlumnus = alumniDaoService.saveAlumni(alumnus);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/alumnibyid/{id}")
                    .buildAndExpand(savedAlumnus.getId())
                    .toUri();


            ControllerLinkBuilder link = linkTo(methodOn(this.getClass()).getAlumniById(savedAlumnus.getId()));
            Resource<AlumniBean> response = new Resource<AlumniBean>(savedAlumnus);
            response.add(link.withRel("user-link"));

            return ResponseEntity.created(location).body(response);
        }
        catch (Exception e) {
            return new ResponseEntity("failed to create alumni", HttpStatus.BAD_REQUEST);
        }
    }

        @GetMapping("/alumnibyname/{name}")
    public Collection<AlumniBean> getAlumniByName(@PathVariable String name) {
        //TODO: resolve this later on

        return AlumniDaoService.getAlumniByName(name);
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
        List<AlumniBean> alumni = new ArrayList<>();
        //alumni.add(new AlumniBean(1l,"John", location));
        //alumni.add(new AlumniBean(2l,"Simon", location));

        return alumni;
    }

}
