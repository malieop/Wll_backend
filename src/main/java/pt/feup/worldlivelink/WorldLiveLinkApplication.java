package pt.feup.worldlivelink;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import pt.feup.worldlivelink.Storage.StorageProperties;
import pt.feup.worldlivelink.Storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class WorldLiveLinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorldLiveLinkApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}
