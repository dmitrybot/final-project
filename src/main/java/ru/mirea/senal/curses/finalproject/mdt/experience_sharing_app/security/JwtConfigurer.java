package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenFilter jwtTokenFilter;
    private final HandlerChainExceptionFilter chainExceptionFilter;

    public JwtConfigurer(JwtTokenFilter jwtTokenFilter, HandlerChainExceptionFilter chainExceptionFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.chainExceptionFilter = chainExceptionFilter;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        builder.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(chainExceptionFilter, JwtTokenFilter.class);
    }
}