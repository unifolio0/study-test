package nextstep.service;

import org.springframework.stereotype.Service;

@Service
public class NotAopService {

    public void callNotTransactional(String name) {
        System.out.println("callTransactional: " + name);
    }

    public void callNotAsync(String name) {
        System.out.println("callAsync: " + name);
    }
}
