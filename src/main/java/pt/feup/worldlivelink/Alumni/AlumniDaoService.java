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
        return new ArrayList<>(alumni.values());
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

    public static Optional<AlumniBean> createAlumniBean(Document alumni){
        try {
            JSONObject alumniJson = new JSONObject(alumni.toJson());
            setAlumniLocation(alumniJson);
            //System.out.println(alumniJson.getJSONObject("user").getString("id"));
            AlumniBean alumniBean = new AlumniBean(new Long(alumniJson.getString("user_id")), alumniJson.getJSONObject("user").getString("firstname"), setAlumniLocation(alumniJson));
            return Optional.of(alumniBean);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return Optional.empty();
    }

    public static AlumniBean saveAlumni(final AlumniBean alumnus) {
        alumnus.setId(ids.incrementAndGet());

        alumni.put(alumnus.getId(), alumnus);

        return alumnus;
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

        Map<Long, AlumniBean> initialValues = new HashMap<>();



        try (  MongoClient mongoClient = MongoClients.create(mongoURL)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDataBase);
            MongoCollection collection = database.getCollection(mongoDocument);
            FindIterable<Document> findIterable = collection.find(new Document());
            Block<Document> printBlock = document -> {
                try {
                    JSONObject alumniJson = new JSONObject(document.toJson());
                    setAlumniLocation(alumniJson);
                    //System.out.println(alumniJson.getJSONObject("user").getString("id"));
                    AlumniBean alumniBean = new AlumniBean(new Long(alumniJson.getString("user_id")), alumniJson.getJSONObject("user").getString("firstname"), setAlumniLocation(alumniJson));
                    initialValues.put(alumniBean.getId(), alumniBean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            };
            findIterable.forEach(printBlock);
        }
        this.alumni.putAll(initialValues);

    }
}
