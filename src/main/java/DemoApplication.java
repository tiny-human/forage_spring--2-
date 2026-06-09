import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// SpringBootServletInitializer est nécessaire pour le packaging WAR (JSP)
@SpringBootApplication
@ComponentScan(basePackages = {"controller", "service", "exception"})
@EntityScan(basePackages = {"model"})
@EnableJpaRepositories(basePackages = {"repository"})
public class DemoApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
