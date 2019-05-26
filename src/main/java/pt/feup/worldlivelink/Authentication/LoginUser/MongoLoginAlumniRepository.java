package pt.feup.worldlivelink.Authentication.LoginUser;

import org.springframework.stereotype.Repository;
import pt.feup.worldlivelink.Alumni.AlumniBean;
import pt.feup.worldlivelink.Alumni.AlumniDaoService;

import java.util.Optional;

@Repository
public class MongoLoginAlumniRepository implements LoginAlumniRepository {

    @Override
    public Optional<AlumniBean> findAlumniByUsername(final String username) {
        return AlumniDaoService.getAlumniAuthenticationByUsername(username);
    }

}
