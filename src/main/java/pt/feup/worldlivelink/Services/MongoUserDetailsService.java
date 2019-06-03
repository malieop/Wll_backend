package pt.feup.worldlivelink.Services;

//import com.example.gtommee.rest_tutorial.models.Users;
//import com.example.gtommee.rest_tutorial.repositories.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pt.feup.worldlivelink.Alumni.AlumniBean;
import pt.feup.worldlivelink.Authentication.LoginUser.MongoLoginAlumniRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class MongoUserDetailsService implements UserDetailsService{

    @Autowired
    private MongoLoginAlumniRepository repository = new MongoLoginAlumniRepository();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AlumniBean> oalumni = repository.findAlumniByUsername(username);

        if(!oalumni.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        AlumniBean alumni = oalumni.get();
        List<SimpleGrantedAuthority> authorities_userAuth = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        List<SimpleGrantedAuthority> authorities_userNotAuth = Arrays.asList(new SimpleGrantedAuthority("ROLE_NOTUSER"));
        List<SimpleGrantedAuthority> authorities_admin = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (oalumni.get().getIsadmin().equals("true")) {
            return new User(alumni.getUsername(), alumni.getPassword(), authorities_admin);
        } else if (oalumni.get().getStatus().equals("0")) {
            return new User(alumni.getUsername(), alumni.getPassword(), authorities_userNotAuth);
        } else if (oalumni.get().getStatus().equals("1")) {
            return new User(alumni.getUsername(), alumni.getPassword(), authorities_userAuth);
        }
        return null;
    }

}
