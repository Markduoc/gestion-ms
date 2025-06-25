package gestion.EcoMarket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.github.cdimascio.dotenv.Dotenv;

@EnableFeignClients
@SpringBootApplication
public class EcoMarketApplication {

	public static void main(String[] args) {
		Dotenv.load();
		SpringApplication.run(EcoMarketApplication.class, args);
	}
}
