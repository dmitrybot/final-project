package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security.JwtConfigurer;
import ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security.Permission;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfigurer jwtConfigurer;
    private final UserDetailsService userDetailsService;

    private static final Logger log = Logger.getLogger(SecurityConfig.class);

    public SecurityConfig(JwtConfigurer jwtConfigurer, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.jwtConfigurer = jwtConfigurer;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/auth/login").permitAll()
                .antMatchers(HttpMethod.GET, "/course/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.POST, "/course/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/course/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.PUT, "/course/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.PATCH, "/course/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.GET, "/lesson/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.POST, "/lesson/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/lesson/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.PUT, "/lesson/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.PATCH, "/lesson/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.GET, "/guide/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.POST, "/guide/**").hasAuthority(Permission.ADMIN.getPermission())
                .antMatchers(HttpMethod.DELETE, "/guide/**").hasAuthority(Permission.ADMIN.getPermission())
                .antMatchers(HttpMethod.PUT, "/guide/**").hasAuthority(Permission.ADMIN.getPermission())
                .antMatchers(HttpMethod.PATCH, "/guide/**").hasAuthority(Permission.ADMIN.getPermission())
                .antMatchers(HttpMethod.GET, "/product/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.POST, "/product/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/product/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.PUT, "/product/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.PATCH, "/product/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.GET, "/user/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.POST, "/user/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.POST, "/user/admin").hasAuthority(Permission.ADMIN.getPermission())
                .antMatchers(HttpMethod.DELETE, "/user/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.PUT, "/user/**").hasAuthority(Permission.BASE.getPermission())
                .antMatchers(HttpMethod.PATCH, "/user/**").hasAuthority(Permission.BASE.getPermission())
                .anyRequest()
                .authenticated()
                .and()
                .apply(jwtConfigurer);
        log.info("Security configured");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        log.info("Security global configured");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        log.info("AuthenticationManager bean configured");
        return super.authenticationManagerBean();
    }

    /*@Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver(){
        return new AuthenticationPrincipalArgumentResolver();
    }*/

    @Bean
    protected PasswordEncoder passwordEncoder() {
        log.info("PasswordEncoder bean configured");
        return new BCryptPasswordEncoder(12);
    }
}