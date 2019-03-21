package net.newglobe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class Server1 {

	public static void main(String[] args) {
		SpringApplication.run(Server1.class, args);
	}

}
