package nextstep;

import nextstep.service.AopService;
import nextstep.service.NotAopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private AopService aopService;

    @Autowired
    private NotAopService notAopService;

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        aopService.callTransactional("AOP");
        aopService.callAsync("AOP");
    }
}
