package nextstep.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AopService {

    @Transactional
    public void callTransactional(String name) {
        System.out.println("callTransactional: " + name);
    }

    @Async
    public void callAsync(String name) {
        System.out.println("callAsync: " + name);
    }

    public void callNotTransactional(String name) {
        System.out.println("callTransactional: " + name);
    }

    public void callNotAsync(String name) {
        System.out.println("callAsync: " + name);
    }
}
