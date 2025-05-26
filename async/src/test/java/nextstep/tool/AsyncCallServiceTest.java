package nextstep.tool;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AsyncCallServiceTest {

    @Autowired
    private AsyncCallService asyncCallService;

    @Test
    void 내부_async_메소드_호출() {
        assertThatThrownBy(() -> asyncCallService.내부_async_메소드_호출())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("async 동작 실패");
    }

    @Test
    void 외부_async_메소드_호출() {
        assertThatCode(() -> asyncCallService.외부_async_메소드_호출())
                .doesNotThrowAnyException();
    }
}
