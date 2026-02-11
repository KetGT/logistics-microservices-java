package ar.edu.utn.frc.rutas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RutasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RutasServiceApplication.class, args);
	}

}
