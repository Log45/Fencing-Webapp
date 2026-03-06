package csh.log.fencingreferee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FencingrefereeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FencingrefereeApplication.class, args);
	}

}
