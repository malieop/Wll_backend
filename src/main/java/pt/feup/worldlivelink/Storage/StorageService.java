package pt.feup.worldlivelink.Storage;

import org.springframework.web.multipart.MultipartFile;
import pt.feup.worldlivelink.Photo.PhotoBean;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(final MultipartFile file, final String alumni_id);

    Stream<Path> loadAll();

    Path load(String filename);

    Optional<PhotoBean> loadAsResource(final String alumni_id);

    void deleteAll();
}
