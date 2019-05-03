package pt.feup.worldlivelink.Alumni;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AlumniDaoService implements InitializingBean {
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
