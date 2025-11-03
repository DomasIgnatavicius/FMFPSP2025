package lt.ignatavicius.fmfpsp.miestomeras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages ={
                "lt.ignatavicius.fmfpsp.miestomeras.rest.controller",
                "lt.ignatavicius.fmfpsp.miestomeras.service",
                "lt.ignatavicius.fmfpsp.miestomeras.config",
                "lt.ignatavicius.fmfpsp.miestomeras.model"
        }
)
public class MiestoMerasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiestoMerasApplication.class, args);
    }

}
