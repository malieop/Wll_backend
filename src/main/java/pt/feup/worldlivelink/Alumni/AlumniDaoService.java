package pt.feup.worldlivelink.Alumni;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pt.feup.worldlivelink.Company.CompanyBean;
import pt.feup.worldlivelink.Location.LocationBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.mongodb.client.model.Filters.*;

@Component
public class AlumniDaoService implements InitializingBean {


    private static Map<Long, AlumniBean> alumni = new ConcurrentHashMap<>();
    private static AtomicLong ids = new AtomicLong();

    public static Optional<AlumniBean> getAlumniById(final String alumni_id) {

        if (!StringUtils.hasText(alumni_id)) {
            return Optional.empty();
        }

        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);

            BasicDBObject query = new BasicDBObject("_id",new ObjectId(alumni_id));
            FindIterable<Document> findIterable = collection.find(query);

            for (Document alumni : findIterable){
                Optional<AlumniBean> alumniBean = createAlumniBean(alumni);
                if(alumniBean.isPresent()){
                    return alumniBean;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();

    }

    public static Collection<AlumniBean> getAlumni() {
        ArrayList<AlumniBean> alumnis = new ArrayList<>();

        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);

            FindIterable<Document> findIterable =  collection.find(eq("user.status", "1"));

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
        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);

            FindIterable<Document> findIterable =  collection.find(and(eq("user.status", "1"),or(regex("user.lastname",name, "i"),regex("user.firstname",name, "i"))));
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

    public static Collection<AlumniBean> getAlumniByLocation(final String location){
        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);

            FindIterable<Document> findIterable =  collection.find(and(eq("user.status", "1"),or(regex("user.location,address",location,"i"),regex("user.location.address",location,"i"))));
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
        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);

            FindIterable<Document> findIterable =  collection.find(and(eq("user.status", "1"),or(regex("user.course.",course, "i"),regex("user.couse.name",course, "i"))));
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
        ArrayList<AlumniBean> alumnis = new ArrayList<>();

        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {

            MongoCollection collection = MongoHelper.getCollection(mongoClient);
            FindIterable<Document> findIterable =  collection.find(and(eq("user.status", "1"),or(eq("user.course.courseyear.startdate", year),eq("user.course.courseyear.enddate", year))));
            for (Document alumni : findIterable){
                Optional<AlumniBean> alumniBean = createAlumniBean(alumni);
                if(alumniBean.isPresent()){
                    alumnis.add(alumniBean.get());
                }
            }
            return alumnis;
        }
    }

    public static Optional<AlumniBean> getAlumniByUsername(final String username){



        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);

            FindIterable<Document> findIterable =  collection.find(or(eq("user.username", username)));

            for (Document alumni : findIterable){
                Optional<AlumniBean> alumniBean = createAlumniBean(alumni);
                if(alumniBean.isPresent()){
                    return alumniBean;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();

    }

    public static Optional<AlumniBean> getAlumniAuthenticationByUsername(final String username){

        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);

            FindIterable<Document> findIterable =  collection.find(or(eq("user.username", username)));

            for (Document alumni : findIterable){
                Optional<AlumniBean> alumniBean = getUsernameAndPassword(alumni);
                if(alumniBean.isPresent()){
                    return alumniBean;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();

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

    public static Optional<AlumniBean> getUsernameAndPassword(Document alumni){
        try {
            JSONObject alumniJson = new JSONObject(alumni.toJson());
            setAlumniLocation(alumniJson);

            JSONObject user =  alumniJson.getJSONObject("user");

            AlumniBean alumniBean = new AlumniBean();
            alumniBean.setId(alumniJson.getJSONObject("_id").getString("$oid"));
            alumniBean.setPassword(user.getString("password"));
            alumniBean.setUsername(user.getString("username"));

            return Optional.of(alumniBean);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return Optional.empty();
    }

    public static void saveAlumni(final AlumniRequestBean alumnus) {
        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);

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
                                            .append("status", "0")
                                            .append("username", alumnus.getUsername())
                                            .append("password", BCrypt.hashpw(alumnus.getPassword(), BCrypt.gensalt())

                                            )
                                    );
            try {
                collection.insertOne(jsonAlumni);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // TODO: implement this
    public static boolean deleteAlumni(final String id) {

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

    public static Collection<AlumniBean> getAlumniNoActivated() {
        ArrayList<AlumniBean> alumnis = new ArrayList<>();
        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);

            FindIterable<Document> findIterable =  collection.find(eq("user.status", "0"));
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

    public void validateAlumni (String id) {
        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);
            BasicDBObject query = new BasicDBObject("_id",new ObjectId(id));
            collection.updateOne(query, Updates.set("user.status","1"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void denyAlumni(String id) {
        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);
            BasicDBObject query = new BasicDBObject("_id",new ObjectId(id));
            collection.updateOne(query, Updates.set("user.status","2"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() {
    }



}
