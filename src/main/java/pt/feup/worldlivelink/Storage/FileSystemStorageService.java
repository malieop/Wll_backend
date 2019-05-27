package pt.feup.worldlivelink.Storage;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pt.feup.worldlivelink.Alumni.AlumniBean;
import pt.feup.worldlivelink.Alumni.AlumniDaoService;
import pt.feup.worldlivelink.Alumni.MongoHelper;
import pt.feup.worldlivelink.Photo.PhotoBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.regex;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(final MultipartFile file, final String alumni_id) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException("Cannot store file with relative path outside current directory "
                                + filename);
            }

            Optional<AlumniBean> optionalAlumniBean = AlumniDaoService.getAlumniById(alumni_id);
            if (!optionalAlumniBean.isPresent()) {
                throw new StorageException("Alumni not found. Failed to store file " + filename);
            }


            FileSystemStorageService.withoutUsingGridFS(filename, alumni_id, file.getBytes());
        }
        catch (Exception e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Optional<PhotoBean> loadAsResource(final String alumni_id) {
        try (MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getFilesCollection(mongoClient);


            return retrieve(alumni_id, collection);

        }
        catch (Exception e) {
            System.out.println("Could not read alumni photo: " + alumni_id);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void withoutUsingGridFS(final String filename, final String alumni_id, final byte b[]) {
        try (MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getFilesCollection(mongoClient);

            Binary data = new Binary(b);
            BasicDBObject o = new BasicDBObject();
            o.append("photo", data);

            Document jsonAlumni = new Document("file" ,
                    new Document("alumni_id", alumni_id)
                            .append("photo", o)
                            .append("filename", filename)
            );



            collection.insertOne(jsonAlumni);

            System.out.println(" file saved to Mongo");

        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    private Optional<PhotoBean> retrieve(final String alumniId, final MongoCollection collection) {

        try {
            FindIterable<Document> findIterable = collection.find(or(regex("file.alumni_id", alumniId, "i")));

            for (Document photo : findIterable){

                if (photo.containsKey("file")) {
                    Document file = (Document) photo.get("file");

                    return Optional.of(new PhotoBean()
                                        .setAlumni_id((String) file.get("alumni_id"))
                                        .setFilename((String) file.get("filename"))
                                        .setBytes(((Binary)((Document) file.get("photo")).get("photo")).getData())
                            );
                }
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
