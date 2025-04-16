package be.restiau.interactivespeciesatlas_v3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class InteractiveSpeciesAtlasV3Application {

    public static void main(String[] args) {
        SpringApplication.run(InteractiveSpeciesAtlasV3Application.class, args);
    }

}
