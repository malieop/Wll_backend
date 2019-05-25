package pt.feup.worldlivelink.Alumni;

import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import pt.feup.worldlivelink.Company.CompanyBean;
import pt.feup.worldlivelink.Location.LocationBean;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.mongodb.client.model.Filters.*;

@Component
public class AlumniDaoService implements InitializingBean {
    private static final String mongoURL = "mongodb+srv://diogocoelho:lgp5d2019@mongocloud-jf8zo.azure.mongodb.net/test?retryWrites=true";
    private static final String mongoDataBase = "LGP5D";
    private static final String mongoDocument = "users";

    private static Map<Long, AlumniBean> alumni = new ConcurrentHashMap<>();
    private static AtomicLong ids = new AtomicLong();

    public static AlumniBean getAlumniById(Long id) {
        return alumni.get(id);
    }

    public static Collection<AlumniBean> getAlumni() {
        ArrayList<AlumniBean> alumnis = new ArrayList<>();

        try (  MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            FindIterable<Document> findIterable =  collection.find();
            //findIterable.iterator().forEach(alumni -> alumnis::add);
            for (Document alumni : findIterable){
                Optional<AlumniBean> alumniBean = createAlumniBean(alumni);
                if(alumniBean.isPresent()){
                    alumnis.add(alumniBean.get());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return alumnis;
    }

    public static Collection<AlumniBean> getAlumniByName(final String name){
        try (  MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            FindIterable<Document> findIterable =  collection.find(or(regex("user.lastname",name, "i"),regex("user.firstname",name, "i")));
            ArrayList<AlumniBean> alumnis = new ArrayList<>();
            //findIterable.iterator().forEach(alumni -> alumnis::add);
            for (Document alumni : findIterable){
                Optional<AlumniBean> alumniBean = createAlumniBean(alumni);
                if(alumniBean.isPresent()){
                    alumnis.add(alumniBean.get());
                }
            }
            return alumnis;
        }

    }

    public static Collection<AlumniBean> getAlumniByLocation(final String location){
        try (  MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            FindIterable<Document> findIterable =  collection.find(or(regex("user.location,address",location,"i"),regex("user.location.address",location,"i")));
            ArrayList<AlumniBean> alumnis = new ArrayList<>();
            for (Document alumni : findIterable){
                Optional<AlumniBean> alumniBean = createAlumniBean(alumni);
                if(alumniBean.isPresent()){
                    alumnis.add(alumniBean.get());
                }
            }
            return alumnis;
        }
    }
    public static Collection<AlumniBean> getAlumniByCourse(final String course){
        try (  MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            FindIterable<Document> findIterable =  collection.find(or(regex("user.course.",course, "i"),regex("user.course.name",course, "i")));
            ArrayList<AlumniBean> alumnis = new ArrayList<>();
            for (Document alumni : findIterable){
                Optional<AlumniBean> alumniBean = createAlumniBean(alumni);
                if(alumniBean.isPresent()){
                    alumnis.add(alumniBean.get());
                }
            }
            return alumnis;
        }
    }
    public static Collection<AlumniBean> getAlumniByYear(final String year){
        try (  MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            FindIterable<Document> findIterable =  collection.find(or(eq("user.course.courseyear.startdate", year),eq("user.course.courseyear.enddate", year)));
            ArrayList<AlumniBean> alumnis = new ArrayList<>();
            for (Document alumni : findIterable){
                Optional<AlumniBean> alumniBean = createAlumniBean(alumni);
                if(alumniBean.isPresent()){
                    alumnis.add(alumniBean.get());
                }
            }
            return alumnis;
        }
    }

    public static Optional<AlumniBean> createAlumniBean(Document alumni){
        //TODO MUDAR O CONSTRUTOR DO ALUMNIBEAN PARA DEVOLVER ISTO DIREITO
        try {
            JSONObject alumniJson = new JSONObject(alumni.toJson());
            setAlumniLocation(alumniJson);
            //System.out.println(alumniJson.getJSONObject("user").getString("id"));

            JSONObject user =  alumniJson.getJSONObject("user");
            AlumniBean alumniBean = new AlumniBean(alumniJson.getJSONObject("_id").getString("$oid"),
                                                   user.getString("name"),
                                                   setAlumniLocation(alumniJson));

            JSONObject companyJson = user.getJSONObject("company");
            CompanyBean company = new CompanyBean()
                    .setName(companyJson.getString("name"))
                    .setEmail(companyJson.getString("email"))
                    .setJob(companyJson.getString("job"))
                    .setStartDate(companyJson.getString("startdate"))
                    ;

            alumniBean.setCompany(company);
            alumniBean.setEmail(user.getString("email"));

            JSONObject courseJson = user.getJSONObject("course");
            UserCourseBean course = new UserCourseBean()
                    .setName(courseJson.getString("name"))
                    .setUniversity(courseJson.getString("university"))
                    .setStartDate(courseJson.getString("startdate"))
                    .setEndDate(courseJson.getString("enddate"))
                    ;

            alumniBean.setCourse(course);

            return Optional.of(alumniBean);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return Optional.empty();
    }

    public static void saveAlumni(final AlumniRequestBean alumnus) {
        try (  MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            Document jsonAlumni = new Document("user" ,
                                    new Document("name", alumnus.getName())
                                            .append("email", alumnus.getEmail())
                                            .append("location",
                                                new Document("address", alumnus.getLocation().getLocation())
                                                    .append("city", alumnus.getLocation().getCity())
                                                    .append("latitude", alumnus.getLocation().getLatitude())
                                                    .append("longitude", alumnus.getLocation().getLongitude()))
                                            .append("company",
                                                new Document("name", alumnus.getCompany().getName())
                                                    .append("email", alumnus.getCompany().getEmail())
                                                    .append("job", alumnus.getCompany().getJob())
                                                    .append("startdate", alumnus.getCompany().getStartDate()))
                                            .append("course",
                                                new Document("name", alumnus.getCourse().getName())
                                                    .append("university", alumnus.getCourse().getUniversity())
                                                    .append("startdate",  alumnus.getCourse().getStartDate())
                                                    .append("enddate",    alumnus.getCourse().getEndDate())
                                            )
                                    );
            try {
                collection.insertOne(jsonAlumni);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static boolean deleteAlumni(final Long id) {

        AlumniBean alumnus = alumni.remove(id);

        return alumnus != null;
    }


    public static LocationBean setAlumniLocation(JSONObject json){
        LocationBean location = new LocationBean();
        try {
            location.setCity(json.getJSONObject("user").getJSONObject("location").getString("city"));
            location.setLocation(json.getJSONObject("user").getJSONObject("location").getString("address"));
            location.setLongitude(json.getJSONObject("user").getJSONObject("location").getString("longitude"));
            location.setLatitude(json.getJSONObject("user").getJSONObject("location").getString("latitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return location;

    }

    @Override
    public void afterPropertiesSet() {
    }
}
