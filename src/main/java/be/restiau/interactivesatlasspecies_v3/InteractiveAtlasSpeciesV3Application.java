package be.restiau.interactivesatlasspecies_v3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class InteractiveAtlasSpeciesV3Application {

    public static void main(String[] args) {
        SpringApplication.run(InteractiveAtlasSpeciesV3Application.class, args);
    }

}
