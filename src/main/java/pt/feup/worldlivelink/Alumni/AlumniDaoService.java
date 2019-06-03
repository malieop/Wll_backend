package pt.feup.worldlivelink.Alumni;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import io.jsonwebtoken.*;
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

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.mongodb.client.model.Filters.*;

@Component
public class AlumniDaoService implements InitializingBean {

//TODO a seguir voltar a por os users validados
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

            FindIterable<Document> findIterable =  collection.find(eq("user.isadmin", "false"));//eq("user.status", "1"));

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

            FindIterable<Document> findIterable =  collection.find(/*and(eq("user.status", "1"),*/and(or(regex("user.lastname",name, "i"),regex("user.firstname",name, "i")),eq("user.isadmin", "false")));//);
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

            //FindIterable<Document> findIterable =  collection.find(and(eq("user.status", "1"),or(regex("user.location,address",location,"i"),regex("user.location.address",location,"i"))));
            FindIterable<Document> findIterable =  collection.find(and(or(regex("user.location,address",location,"i"),regex("user.location.address",location,"i")), eq("user.isadmin","false")));

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
            FindIterable<Document> findIterable =  collection.find(and(or(regex("user.course.",course, "i"),regex("user.couse.name",course, "i")),eq("user.isadmin","false")));

            //FindIterable<Document> findIterable =  collection.find(and(eq("user.status", "1"),or(regex("user.course.",course, "i"),regex("user.couse.name",course, "i"))));
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
            //FindIterable<Document> findIterable =  collection.find(and(eq("user.status", "1"),or(eq("user.course.courseyear.startdate", year),eq("user.course.courseyear.enddate", year))));

            FindIterable<Document> findIterable =  collection.find(and(or(eq("user.course.courseyear.startdate", year),eq("user.course.courseyear.enddate", year)),eq("user.isadmin","false")));
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
                    setAlumniLocation(alumniJson),
                    user.getString("isadmin"),
                    user.getString("status"));

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

    public static boolean saveAlumni(final AlumniRequestBean alumnus) {
        boolean usernameRegistered = false;
        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);
            FindIterable<Document> result = collection.find(or(eq("user.username",alumnus.getUsername()),eq("user.email",alumnus.getEmail())));
            for (Document alumni: result){
                usernameRegistered = true;
            }
            if(usernameRegistered == false) {
                Document jsonAlumni = new Document("user",
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
                                                .append("startdate", alumnus.getCourse().getStartDate())
                                                .append("enddate", alumnus.getCourse().getEndDate())
                                )
                                .append("status", "0")
                                .append("isadmin", "false")
                                .append("username", alumnus.getUsername())
                                .append("password", BCrypt.hashpw(alumnus.getPassword(), BCrypt.gensalt())

                                )
                );
                try {
                    collection.insertOne(jsonAlumni);
                    return true;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            return false;

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

            FindIterable<Document> findIterable =  collection.find(and(eq("user.status", "0"),eq("user.isadmin","false")));
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

    public static void updateAlumni(AlumniUpdateInformationBean alumni) {
        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);

            if (!alumni.getName().isEmpty() && !alumni.getName().equals("")) {
                collection.updateOne(eq("user.username",alumni.username), Updates.set("user.name",alumni.getName()));
            }
            if (!alumni.getLocation().getLocation().isEmpty() && !alumni.getLocation().getLocation().equals("")) {
                collection.updateOne(eq("user.username",alumni.username), Updates.set("user.location.address",alumni.getLocation().getLocation()));

            }
            if (!alumni.getLocation().getLatitude().isEmpty() && !alumni.getLocation().getLatitude().equals("")) {
                collection.updateOne(eq("user.username",alumni.username), Updates.set("user.location.latitude",alumni.getLocation().getLatitude()));

            }
            if (!alumni.getLocation().getLongitude().isEmpty() && !alumni.getLocation().getLongitude().equals("")) {
                collection.updateOne(eq("user.username",alumni.username), Updates.set("user.location.longitude",alumni.getLocation().getLongitude()));

            }
            if (!alumni.getCourse().getName().isEmpty() && !alumni.getCourse().getName().equals("")) {
                collection.updateOne(eq("user.username",alumni.username), Updates.set("user.course.name",alumni.getCourse().getName()));

            }
            if (!alumni.getCompany().getJob().isEmpty() && !alumni.getCompany().getJob().equals("")) {
                collection.updateOne(eq("user.username",alumni.username), Updates.set("user.company.job",alumni.getCompany().getJob()));
            }
            if (!alumni.getCompany().getName().isEmpty() && !alumni.getCompany().getName().equals("")){
                collection.updateOne(eq("user.username", alumni.username), Updates.set("user.company.name", alumni.getCompany().getName()));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setAdminAlumni(String id) {
        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);
            BasicDBObject query = new BasicDBObject("_id",new ObjectId(id));
            collection.updateOne(query, Updates.set("user.isadmin","true"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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

    // TOKEN
    public static String createJWT(String id, long expirationMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("LGP5D");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE).setId(id)
                .setIssuedAt(now)
                .claim("user_id",id)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (expirationMillis >= 0) {
            long expMillis = nowMillis + expirationMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    private void parseJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("LGP5D"))
                .parseClaimsJws(jwt).getBody();
        System.out.println("ID: " + claims.get("user_id"));
        System.out.println("Expiration: " + claims.getExpiration());
    }

    public Collection<AlumniBean> getMapAlumni() {
        ArrayList<AlumniBean> alumnis = new ArrayList<>();

        try (  MongoClient mongoClient = MongoHelper.getMongoClient()) {
            MongoCollection collection = MongoHelper.getCollection(mongoClient);

            FindIterable<Document> findIterable =  collection.find(eq("user.isadmin","false"));//and(eq("user.status", "1")),eq("user.isadmin","false"));

            for (Document alumni : findIterable){
                Optional<AlumniBean> alumniBean = createAlumniBean(alumni);
                if(alumniBean.isPresent() && !alumniBean.get().getLocation().getLongitude().isEmpty()){
                    alumnis.add(alumniBean.get());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return alumnis;
    }

    @Override
    public void afterPropertiesSet() {
    }



}
