package ch.uzh.ifi.seal.soprafs15;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * WebMvcConfigurationSupport excluded since JacksonConfig sets some specific configs
 */
@ComponentScan
@EnableAutoConfiguration(exclude={WebMvcConfigurationSupport.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
