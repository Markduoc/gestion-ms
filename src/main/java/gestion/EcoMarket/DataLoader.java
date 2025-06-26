package gestion.EcoMarket;

import gestion.EcoMarket.Model.Mensaje;
import gestion.EcoMarket.Repository.MensajeRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Override
    public void run(String... args) {
        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            Mensaje mensaje = new Mensaje();
            mensaje.setTitulo(faker.book().title());
            mensaje.setDescripcion(faker.lorem().sentence());
            mensaje.setCuerpo(faker.lorem().paragraph(2));
            mensaje.setActivo(random.nextBoolean());
            
            mensaje.setIdUsuario(Long.valueOf(random.nextInt(5) + 1));

            mensajeRepository.save(mensaje);
        }

        System.out.println("Mensajes de prueba cargados (perfil dev)");
    }
}