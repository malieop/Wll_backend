package pt.feup.worldlivelink.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.feup.worldlivelink.Course.CourseBean;
import pt.feup.worldlivelink.Course.CourseDaoService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
public class CourseController {

    @Autowired
    private static CourseDaoService courseDaoService = new CourseDaoService();

    @GetMapping("/courses")
    public Collection<CourseBean> getListCourse() {
        return courseDaoService.getCourse();
    }

    @GetMapping("/coursebyname/{name}")
    public Collection<CourseBean> getcourseByName(@PathVariable String name) {

        return courseDaoService.getCourseByName(name);
    }

    @GetMapping("/coursebytype/{type}")
    public Collection<CourseBean> getcourseByType(@PathVariable String type) {

        return courseDaoService.getCourseByType(type);
    }

    @GetMapping("/coursebyinitials/{initials}")
    public Collection<CourseBean> getcourseByInitials(@PathVariable String initials) {

        return courseDaoService.getCourseByInitials(initials);
    }

}
