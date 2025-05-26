package nextstep.tool;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AsyncCallServiceTest {

    @Autowired
    private AsyncCallService asyncCallService;

    @Test
    void callInnerAsyncMethod() {
        asyncCallService.callInnerAsyncMethod();
    }

    @Test
    void callAsyncMethod() {
        asyncCallService.callAsyncMethod();
    }
}
