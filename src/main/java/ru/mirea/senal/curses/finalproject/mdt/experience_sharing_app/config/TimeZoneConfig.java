package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {

    private static final Logger log = Logger.getLogger(TimeZoneConfig.class);

    @Bean
    public TimeZone timeZone() {
        TimeZone defaultTimeZone = TimeZone.getTimeZone("UTC");
        TimeZone.setDefault(defaultTimeZone);
        log.info("Security configured");
        return defaultTimeZone;
    }
}