package uk.gov.justice.laa.crime.cfecrime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.gov.justice.laa.crime.cfecrime.interfaces.Generated;

@SpringBootApplication
@Generated
public class CfeCrimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CfeCrimeApplication.class, args);
	}

}
