package nextstep.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    @Async
    public void callAsync(String name) {
        System.out.println("callAsync: " + name);
    }

    public void callNotAsync(String name) {
        System.out.println("callAsync: " + name);
    }
}
