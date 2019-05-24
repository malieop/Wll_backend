package pt.feup.worldlivelink.config;

//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;

//@Slf4j
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // TODO: remove this
    // hardcoded users for testing
//    @Override
//    public void configure(AuthenticationManagerBuilder auth)
//            throws Exception {
//
//        auth.inMemoryAuthentication()
//                .withUser( User.withDefaultPasswordEncoder()
//                               .username("user")
//                               .password("password")
//                               .roles("USER"));

//        auth.inMemoryAuthentication()
//                .withUser( User.withDefaultPasswordEncoder()
//                               .username("admin")
//                               .password("password")
//                               .roles("ADMIN")
//                );
//
//    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.authorizeRequests()
//                .antMatchers("/css/**", "/webjars/**", "/login").permitAll()
////                .antMatchers("/**").hasRole("ADMIN")
//                .antMatchers("/**").hasRole("USER")
//                .and()
//
//                .formLogin().loginPage("/login")
//                .and()
//                .httpBasic().realmName("securityintro");
//
//        // TODO: remove this, only here for testing
//        http.csrf().disable();
//    }

//}

