package gestion.EcoMarket;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcoMarketApplication {

	public static void main(String[] args) {
		Dotenv.load();
		SpringApplication.run(EcoMarketApplication.class, args);
	}

}
