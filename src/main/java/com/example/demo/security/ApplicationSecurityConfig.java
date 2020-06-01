package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.concurrent.TimeUnit;

import static com.example.demo.security.ApplicationUserPermissions.*;
import static com.example.demo.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http);
        http.
                csrf().disable().
                /*csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                .and().*/
                authorizeRequests().
                antMatchers("/","index","/css/*","/js/*").permitAll().
                antMatchers("/api/v1/**").hasRole(ApplicationUserRole.STUDENT.name()).
                /*antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission()).
                antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission()).
                antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission()).
                antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name(),TRAINEEADMIN.name()).*/
                anyRequest().authenticated()
                .and().
                //httpBasic();
                formLogin().loginPage("/login").permitAll()
                .and().rememberMe()
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21 )).
                key("keyToStoreUserNameAndCustomTimeMd5Hash").
                and().logout().
                logoutUrl("/logout").
                clearAuthentication(true).invalidateHttpSession(true).deleteCookies("JSESSIONID","remember-me").
                logoutSuccessUrl("/login");
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        //return super.userDetailsService();
        //setting password like this - password("pwd"). wont work - will throw No password encoder found exception
        UserDetails cersieUser = User.builder().
                username("cersie").
                password(passwordEncoder.encode("password")).
                //roles(ApplicationUserRole.STUDENT.name()).
                        authorities(STUDENT.getGrantedAuthorities()).
                build();

        UserDetails tyrionUser = User.builder().
                username("tyrion").
                password(passwordEncoder.encode("password123")).
                //roles(ApplicationUserRole.ADMIN.name()).
                        authorities(ADMIN.getGrantedAuthorities()).
                build();

        UserDetails jamieUser = User.builder().
                username("jamie").
                password(passwordEncoder.encode("password123")).
                //roles(ApplicationUserRole.TRAINEEADMIN.name()).
                        authorities(TRAINEEADMIN.getGrantedAuthorities()).
                build();

        return new InMemoryUserDetailsManager(
                cersieUser,tyrionUser,jamieUser
        );
    }
}
