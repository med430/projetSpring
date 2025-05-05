package gl2.example.salles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class SallesApplication {

    public static void main(String[] args) {

        String address = "0.0.0.0";
        String port = "8080";

        for(int i = 0; i<args.length; i++) {
            if(args[i].equals("--address") && i+1 < args.length) {
                address = args[i+1].equals("localhost")? "127.0.0.1" : args[i+1];
            } else if(args[i].equals("--port") && i+1 < args.length) {
                port = args[i+1];
            }
        }

        SpringApplication app = new SpringApplication(SallesApplication.class);
        app.setDefaultProperties(Map.of(
                "server.address", address,
                "server.port", port
        ));
        app.run(args);
    }

}
