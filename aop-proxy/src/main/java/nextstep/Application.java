package nextstep;

import nextstep.service.AopService;
import nextstep.service.NotAopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

// 만약 EnableAsync 가 없으면 AsyncService의 프록시가 생성되지 않음
@EnableAsync
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
