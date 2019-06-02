package pt.feup.worldlivelink.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pt.feup.worldlivelink.Alumni.AlumniBean;
import pt.feup.worldlivelink.Alumni.AlumniDaoService;
import pt.feup.worldlivelink.Alumni.AlumniRequestBean;
import pt.feup.worldlivelink.Alumni.AlumniUpdateInformationBean;

import javax.annotation.processing.SupportedOptions;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;


@CrossOrigin
@RestController
public class AlumniController {

    @Autowired
    private static AlumniDaoService alumniDaoService = new AlumniDaoService();

    @GetMapping("/alumni")
    public Collection<AlumniBean> getListAlumni() {
        return alumniDaoService.getAlumni();
    }
    @GetMapping("/alumnimap")
    public Collection<AlumniBean> getMapListAlumni() {
        return alumniDaoService.getMapAlumni();
    }

    // only application/json, application/*+json, application/json, application/*+json formats supported
    //@CrossOrigin(allowedHeaders = "", allowCredentials = "true", methods = {RequestMethod.POST}, exposedHeaders = )
    @PostMapping("/alumni")
    public ResponseEntity<Object> createAlumni(final @Valid @RequestBody AlumniRequestBean alumnus) {
        boolean regist;
        if(!alumnus.getUsername().isEmpty() && !alumnus.getPassword().isEmpty() && !alumnus.getEmail().isEmpty() && !alumnus.getName().isEmpty()) {
            try {
                regist = alumniDaoService.saveAlumni(alumnus);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity("failed to create alumni", HttpStatus.BAD_REQUEST);
            }
            if (regist) {
                return new ResponseEntity("Created alumni", HttpStatus.OK);
            } else {
                return new ResponseEntity("Alumni username already exists", HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity("Fields are not filled", HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/alumni", method = RequestMethod.OPTIONS)
    protected void  optionsAlumni(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER,Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
        res.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        if ("OPTIONS".equals(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }
    //EndPoint Para mudar o Status do Alumni de 0 para 1(0 = não autorizado, 1 =  Autorizado)
    @PutMapping("/validatealumni/{id}")
    public ResponseEntity<Object> updateAlumni(final @PathVariable String id ) {
        try {
            alumniDaoService.validateAlumni(id);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("failed to update Alumni Status", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Updated alumni status", HttpStatus.OK);
    }
    @PutMapping("/denyalumni/{id}")
    public ResponseEntity<Object> denyAlumni(final @PathVariable String id ) {
        try {
            alumniDaoService.denyAlumni(id);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("failed to update Alumni Status", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Denied alumni status", HttpStatus.OK);
    }

    @GetMapping("/alumnibyname/{name}")
    public Collection<AlumniBean> getAlumniByName(@PathVariable String name) {

        return AlumniDaoService.getAlumniByName(name);
    }

    @GetMapping("/alumnibyid/{id}")
    public ResponseEntity<Object> getAlumniById(@PathVariable String id) {

        Optional<AlumniBean> optionalAlumniBean = alumniDaoService.getAlumniById(id);

        if (optionalAlumniBean.isPresent()) {
            return ResponseEntity.ok().body(optionalAlumniBean.get());
        }

        return  new ResponseEntity("failed to find alumni", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/alumnibyid/{id}")
    public ResponseEntity<Object> deleteAlumniById(@PathVariable String id) {

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
    @GetMapping("/alumnisnotactivated")
    public Collection<AlumniBean>getAlumniNotActivated(){
        return AlumniDaoService.getAlumniNoActivated();
    }

    @GetMapping("/alumni/getloggeduserinfo")
    public ResponseEntity<AlumniBean> getloggeduserinfo(){

        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
        SecurityContextImpl securityContext = ((SecurityContextImpl) request.getSession(true).getAttribute("SPRING_SECURITY_CONTEXT"));
        String username = ((User) securityContext.getAuthentication().getPrincipal()).getUsername();

        Optional<AlumniBean> optionalAlumniBean = AlumniDaoService.getAlumniByUsername(username);

        if (optionalAlumniBean.isPresent()) {
            return ResponseEntity.ok().body(optionalAlumniBean.get());
        }
        else {
            return new ResponseEntity("failed to get session alumni", HttpStatus.NOT_FOUND);
        }
    }

   /* @GetMapping("/alumnifiltered/{localization}/{year}/{course}")
    public Collection<AlumniBean> getFilteredAlumni(@PathVariable(value = "localization") String localization, @PathVariable(value= "year") String year, @PathVariable(value= "course") String course){
      //TODO Acabar os filtros todos juntos
    }
*/

    @PostMapping("/alumnitoken/{id}")
    public String getToken(@PathVariable String id){
        return AlumniDaoService.createJWT(id,86400000);// 60sec*60min*24hours*1000milisec
    }

    @PutMapping("/updatealumni")
    public ResponseEntity<Object> updateAlumniInfo(final @Valid @RequestBody AlumniUpdateInformationBean alumni){
        try {
            AlumniDaoService.updateAlumni(alumni);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("failed to update Alumni information", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Updated alumni information", HttpStatus.OK);
    }

    @PostMapping("/setAdmin")
    public ResponseEntity<Object> setAdmin(@Valid @RequestBody String id){
        try {
            JSONObject idJson = new JSONObject(id);
            AlumniDaoService.setAdminAlumni(idJson.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
            return new ResponseEntity("failed to update Alumni information", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("failed to update Alumni information", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Updated alumni information", HttpStatus.OK);
    }


}
