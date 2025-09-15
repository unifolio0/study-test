package nextstep.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ExtendWith(DataBaseCleaner.class)
class ExternalTransactionalServiceTest {

    @Autowired
    private ExternalTransactionalService externalTransactionalService;

    @Test
    void readOnlyFalseInvokeReadOnlyFalseMethod() {
        assertThatCode(() -> externalTransactionalService.readOnlyFalseInvokeReadOnlyFalseMethod())
                .doesNotThrowAnyException();
    }

    @Test
    void readOnlyFalseInvokeReadOnlyTrueMethod() {
        assertThatCode(() -> externalTransactionalService.readOnlyFalseInvokeReadOnlyTrueMethod())
                .doesNotThrowAnyException();
    }

    @Test
    void readOnlyTrueInvokeReadOnlyFalseMethod() {
        assertThatThrownBy(() -> externalTransactionalService.readOnlyTrueInvokeReadOnlyFalseMethod())
                .isInstanceOf(Exception.class);
    }

    @Test
    void readOnlyTrueInvokeReadOnlyTrueMethod() {
        assertThatCode(() -> externalTransactionalService.readOnlyTrueInvokeReadOnlyTrueMethod())
                .doesNotThrowAnyException();
    }
}
