package pt.feup.worldlivelink.Course;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import pt.feup.worldlivelink.Course.CourseBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CourseDaoService implements InitializingBean {
    private MongoClient mongoClient = MongoClients.create("mongodb+srv://diogocoelho:lgp5d2019@mongocloud-jf8zo.azure.mongodb.net/test?retryWrites=true");
    private MongoDatabase database = mongoClient.getDatabase("LGP5D");
    private MongoCollection<Document> collection = database.getCollection("courses");

    private static Map<String, CourseBean> courses = new ConcurrentHashMap<>();

    // Return only a course
    public static CourseBean getCourseById(String id) {
        return courses.get(id);
    }

    // Return all the courses
    public static Collection<CourseBean> getCourse() {
        return new ArrayList<>(courses.values());
    }


    @Override
    public void afterPropertiesSet() throws Exception {


    }

}
