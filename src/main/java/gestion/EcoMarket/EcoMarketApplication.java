package gestion.EcoMarket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class EcoMarketApplication {

	public static void main(String[] args) {
		Dotenv.load();
		SpringApplication.run(EcoMarketApplication.class, args);
	}
}
