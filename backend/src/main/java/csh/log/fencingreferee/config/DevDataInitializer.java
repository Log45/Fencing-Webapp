package csh.log.fencingreferee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;

import csh.log.fencingreferee.domain.User;
import csh.log.fencingreferee.persistence.UserRepository;

@Configuration
public class DevDataInitializer {

    @Bean
    CommandLineRunner initDevUser(UserRepository userRepo) {
        return args -> {

            if (userRepo.count() == 0) {

                User dev = new User();
                dev.setUsername("dev");

                userRepo.save(dev);

                System.out.println("Created default dev user.");
            }
        };
    }
}
