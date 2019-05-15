package pt.feup.worldlivelink.Alumni;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AlumniDaoService implements InitializingBean {
    private static final MongoClient mongoClient = MongoClients.create("mongodb+srv://diogocoelho:lgp5d2019@mongocloud-jf8zo.azure.mongodb.net/test?retryWrites=true");
    private static final MongoDatabase database = mongoClient.getDatabase("LGP5D");
    private static final MongoCollection<Document> collection = database.getCollection("users");

    private static Map<Long, AlumniBean> alumni = new ConcurrentHashMap<>();
    private static AtomicLong ids = new AtomicLong();

    public static AlumniBean getAlumniById(Long id) {
        return alumni.get(id);
    }

    public static Collection<AlumniBean> getAlumni() {
        return new ArrayList<>(alumni.values());
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


    @Override
    public void afterPropertiesSet() {

        Map<Long, AlumniBean> initialValues = Stream.of("Brian Eno", "Roger Waters", "Ryuichi Sakamoto", "David Gilmour")
                .map(name -> new AlumniBean(ids.incrementAndGet(), name, "Boston"))
                .collect(Collectors.toMap(AlumniBean::getId, p->p));

        this.alumni.putAll(initialValues);



    }
}
