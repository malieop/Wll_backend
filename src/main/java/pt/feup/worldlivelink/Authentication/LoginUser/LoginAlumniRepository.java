package pt.feup.worldlivelink.Authentication.LoginUser;

import pt.feup.worldlivelink.Alumni.AlumniBean;

import java.util.Optional;

public interface LoginAlumniRepository {

//    AlumniRequestBean save(AlumniRequestBean user);

    Optional<AlumniBean> findAlumniByUsername(final String username);

//    List<AlumniRequestBean> findAll();

//    Optional<AlumniRequestBean> findById(final String l);
}
