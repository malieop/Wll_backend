package pt.feup.worldlivelink.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import pt.feup.worldlivelink.Authentication.*;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
//@RequestMapping("/")
public class UserController {

//    @GetMapping("/LoginUser/{id}")
//    public Collection<User> getListAlumni(@PathVariable Long id) {
//        UserRepository repository = new UserRepositorySample();
//		UserHandler handler = new UserHandler(repository);
//
//        return handler.getAllUsers();
//    }

//    @Autowired
//    SecurityService securityService;

    @ResponseBody
    @RequestMapping("")
    public Map<String, Object> test() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("result", "Aloha");
        return map;
    }

//    @ResponseBody
//    @RequestMapping("/security/generate/token")
//    public Map<String, Object> generateToken(@RequestParam(value = "subject") String subject) {
//
//        String token = securityService.createToken(subject, (15 * 1000 * 60));
//
//        Map<String, Object> map = new LinkedHashMap<>();
//        map.put("result", token);
//
//        return map;
//    }
//
//    @ResponseBody
//    @RequestMapping("/security/get/subject")
//    public Map<String, Object> getSubject(@RequestParam(value = "token") String token) {
//
//        String subject = securityService.getSubject(token);
//
//        Map<String, Object> map = new LinkedHashMap<>();
//        map.put("result", subject);
//
//        return map;
//    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam Optional<String> error) {

        if (error.isPresent()) {
            log.error("error");
            log.info("Incorrect login, warning the LoginUser");
            model.addAttribute("loginError", "true");
        }
        return "login";
    }


}