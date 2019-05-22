package pt.feup.worldlivelink.Alumni;

import com.mongodb.Block;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import pt.feup.worldlivelink.Location.LocationBean;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        try (  MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            FindIterable<Document> findIterable =  collection.find();
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
            FindIterable<Document> findIterable =  collection.find(or(regex("user.course.",course, "i"),regex("user.couse.name",course, "i")));
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
            AlumniBean alumniBean = new AlumniBean(alumniJson.getString("_id"), alumniJson.getJSONObject("user").getString("name"), setAlumniLocation(alumniJson));
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
            String jsonAlumni ="\"user\":{\"name\":\""+alumnus.getName()+"\"," +
                                        "\"email\":\""+alumnus.getEmail()+"\"," +
                    "\"location\":{\"address\":\""+alumnus.getLocation().getLocation()+"\"," +
                                "\"city\":\""+alumnus.getLocation().getCity()+"\"," +
                                "\"latitude\":\""+alumnus.getLocation().getLatitude()+"\"," +
                                "\"longitude\":\""+alumnus.getLocation().getLongitude()+"\"}," +
                    "\"company\":{\"name\":\""+alumnus.getCompanyBean().getName()+"\"," +
                                "\"email\":\""+alumnus.getCompanyBean().getEmail()+"\"," +
                                "\"job\":\""+alumnus.getCompanyBean().getJob()+"\"," +
                                "\"startdate\":\""+alumnus.getCompanyBean().getStartdate()+"\"}," +
                    "\"course\":{\"name\":\""+alumnus.getCourse().getName()+"\"," +
                                "\"university\":\""+alumnus.getCourse().getUniversity()+"\"," +
                                "\"courseyear\":{\"startdate\":\""+alumnus.getCourse().getStartdate()+"\"," +
                                                "\"enddate\":\""+alumnus.getCourse().getEnddate()+"\"}}}";
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
