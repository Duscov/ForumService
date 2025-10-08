package cohort_65.java.forumservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ForumServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumServiceApplication.class, args);
    }

}