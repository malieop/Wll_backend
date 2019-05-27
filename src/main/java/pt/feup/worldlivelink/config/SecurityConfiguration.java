package pt.feup.worldlivelink.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.feup.worldlivelink.Services.MongoUserDetailsService;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    MongoUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .regexMatchers("/",
                        "/webjars/.*", // used by swagger
                        "/swagger-.*", // used by swagger
                        "/css/.*",
                        "/alumni",
                        "/login")
                .permitAll()
                .and()

                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()

                .formLogin().loginPage("/login")
                .and()
                .httpBasic()
        ;

    }

    @Override
    public void configure(WebSecurity web) {
        //TODO REMOVE ALUMNISNOTACTIVATED, VALIDATEALUMNI AND DENYALUMNI FROM HERE
        web.ignoring().antMatchers("/v2/api-docs","/alumnisnotactivated","/validatealumni/{id}","/denyalumni/{id}",
                "/configuration/ui",
                "/swagger-resources",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }



}

