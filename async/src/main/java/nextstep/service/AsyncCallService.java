package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.tool.AsyncProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncCallService {

    private final AsyncProvider asyncProvider;

    public void 외부_async_메소드_호출() {
        asyncProvider.async_메소드_예외_발생();
    }

    public void 내부_async_메소드_호출() {
        innerAsyncMethod();
    }

    @Async
    public void innerAsyncMethod() {
        throw new IllegalArgumentException("async 동작 실패");
    }
}
