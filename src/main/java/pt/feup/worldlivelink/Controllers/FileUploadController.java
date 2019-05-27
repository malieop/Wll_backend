package pt.feup.worldlivelink.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.feup.worldlivelink.Photo.PhotoBean;
import pt.feup.worldlivelink.Storage.StorageFileNotFoundException;
import pt.feup.worldlivelink.Storage.StorageService;

import java.util.Optional;


// This class is annotated with @Controller so Spring MVC can pick it up and look for routes.
@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/photo/")
    public ResponseEntity<Object> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("alumni_id") String id) {

        try {
            storageService.store(file, id);

            return new ResponseEntity("You successfully uploaded " + file.getOriginalFilename() + "!", HttpStatus.OK);

        }
        catch (Exception e) {
            return new ResponseEntity("failed to upload file", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/photo/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> servePhoto(@PathVariable String id) {


        Optional<PhotoBean> ophotoBean = storageService.loadAsResource(id);

        if (ophotoBean.isPresent()) {

            PhotoBean photoBean = ophotoBean.get();

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + photoBean.getFilename()+"\"").body(photoBean.getBytes());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
