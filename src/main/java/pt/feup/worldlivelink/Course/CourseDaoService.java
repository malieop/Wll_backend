package pt.feup.worldlivelink.Course;

import com.mongodb.Block;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import pt.feup.worldlivelink.Alumni.AlumniBean;
import pt.feup.worldlivelink.Course.CourseBean;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.regex;

@Component
public class CourseDaoService implements InitializingBean {
    private static final String mongoURL = "mongodb+srv://diogocoelho:lgp5d2019@mongocloud-jf8zo.azure.mongodb.net/test?retryWrites=true";
    private static final String mongoDataBase = "LGP5D";
    private static final String mongoDocument = "course";


    private static Map<String, CourseBean> courses = new ConcurrentHashMap<>();

    // Return only a course
    public static CourseBean getCourseById(String id) {

        return courses.get(id);
    }

    // Return all the courses

    /*    public static Collection<CourseBean> getCourse() {
            return new ArrayList<>(courses.values());
        }*/

    public static Collection<CourseBean> getCourse(){

        try (MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            FindIterable<Document> findIterable =  collection.find();
            ArrayList<CourseBean> courses = new ArrayList<>();
            //findIterable.iterator().forEach(alumni -> alumnis::add);
            for (Document course : findIterable){
                Optional<CourseBean> courseBean = createCourseBean(course);
                if(courseBean.isPresent()){
                    courses.add(courseBean.get());
                }
            }
            return courses;
        }

    }

    public static Collection<CourseBean> getCourseByName(String name){

        try (MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            FindIterable<Document> findIterable =  collection.find(regex("Name",name, "i"));
            ArrayList<CourseBean> courses = new ArrayList<>();
            //findIterable.iterator().forEach(alumni -> alumnis::add);
            for (Document course : findIterable){
                Optional<CourseBean> courseBean = createCourseBean(course);
                if(courseBean.isPresent()){
                    courses.add(courseBean.get());
                }
            }
            return courses;
        }

    }

    public static Collection<CourseBean> getCourseByType(String type){

        try (MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            FindIterable<Document> findIterable =  collection.find(regex("Type",type, "i"));
            ArrayList<CourseBean> courses = new ArrayList<>();
            //findIterable.iterator().forEach(alumni -> alumnis::add);
            for (Document course : findIterable){
                Optional<CourseBean> courseBean = createCourseBean(course);
                if(courseBean.isPresent()){
                    courses.add(courseBean.get());
                }
            }
            return courses;
        }

    }

    public static Collection<CourseBean> getCourseByInitials(String initials){

        try (MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            FindIterable<Document> findIterable =  collection.find(regex("Initials",initials, "i"));
            ArrayList<CourseBean> courses = new ArrayList<>();
            //findIterable.iterator().forEach(alumni -> alumnis::add);
            for (Document course : findIterable){
                Optional<CourseBean> courseBean = createCourseBean(course);
                if(courseBean.isPresent()){
                    courses.add(courseBean.get());
                }
            }
            return courses;
        }

    }


    public static Optional<CourseBean> createCourseBean(Document course){
        try {
            JSONObject coursesJSON = new JSONObject(course.toJson());
            //System.out.println(alumniJson.getJSONObject("user").getString("id"));
            CourseBean courseBean = new CourseBean(coursesJSON.getString("_id"),coursesJSON.getString("Name"),coursesJSON.getString("Initials"),coursesJSON.getString("Type"));
            return Optional.of(courseBean);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return Optional.empty();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Map<String, CourseBean> initialValues = new HashMap<>();



        try (MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            FindIterable<Document> findIterable = collection.find(new Document());
            Block<Document> printBlock = document -> {
                try {
                    JSONObject coursesJSON = new JSONObject(document.toJson());
                    //System.out.println(alumniJson.getJSONObject("user").getString("id"));
                    CourseBean courseBean = new CourseBean(coursesJSON.getString("_id"),coursesJSON.getString("Name"),coursesJSON.getString("Initials"),coursesJSON.getString("Type"));
                    initialValues.put(courseBean.getId(), courseBean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            };
            findIterable.forEach(printBlock);
        }
        this.courses.putAll(initialValues);

    }

}
