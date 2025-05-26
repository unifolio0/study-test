package nextstep.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncCallService {

    private final AsyncService asyncService;

    public AsyncCallService(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    public void callAsyncMethod() {
        log.info("async method start");
        asyncService.asyncMethod();
        log.info("async method called");
    }

    public void callInnerAsyncMethod() {
        log.info("async method start");
        innerAsyncMethod();
        log.info("inner async method called");
    }

    @Async
    public void innerAsyncMethod() {
        for (int i = 0; i < 5; i++) {
            log.info("processing async method: {}", i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
