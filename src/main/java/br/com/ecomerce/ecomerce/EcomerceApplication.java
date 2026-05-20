package br.com.ecomerce.ecomerce;


import br.com.ecomerce.ecomerce.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class EcomerceApplication implements CommandLineRunner {

    @Autowired
    private DBService dbService;

	public static void main(String[] args) {
		SpringApplication.run(EcomerceApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        dbService.instantiateTestDatabase();

    }
}
