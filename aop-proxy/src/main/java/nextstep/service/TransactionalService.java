package nextstep.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionalService {

    @Transactional
    public void callTransactional(String name) {
        System.out.println("callTransactional: " + name);
    }

    public void callNotTransactional(String name) {
        System.out.println("callTransactional: " + name);
    }
}
