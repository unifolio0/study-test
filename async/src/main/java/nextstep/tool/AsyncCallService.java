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

    public void 외부_async_메소드_호출() {
        asyncService.asyncMethod();
    }

    public void 내부_async_메소드_호출() {
        innerAsyncMethod();
    }

    @Async
    public void innerAsyncMethod() {
        throw new IllegalArgumentException("async 동작 실패");
    }
}
