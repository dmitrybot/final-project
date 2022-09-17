package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app")
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

}
