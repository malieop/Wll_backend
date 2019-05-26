package pt.feup.worldlivelink.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class UserController {

    @ResponseBody
    @RequestMapping("")
    public Map<String, Object> test() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("result", "Aloha");
        return map;
    }

    @GetMapping("/login")
    public ResponseEntity<Resource> login(Model model, @RequestParam Optional<String> error) {

        if (error.isPresent()) {
            log.error("error");
            log.info("Incorrect login, warning the LoginUser");
            model.addAttribute("loginError", "true");
            return new ResponseEntity("authentication failed", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity("authentication success", HttpStatus.OK);
    }


}