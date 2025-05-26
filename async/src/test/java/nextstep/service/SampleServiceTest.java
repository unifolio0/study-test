package nextstep.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.domain.Sample;
import nextstep.repository.SampleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "/rollback.sql")
@SpringBootTest
class SampleServiceTest {

    @Autowired
    private SampleService sampleService;
    @Autowired
    private SampleRepository sampleRepository;

    @Test
    void Transactional_달린_메소드_내부에서_호출한_async_메소드에서_예외_발생해도_롤백이_발생하지_않는다() {
        assertThat(sampleRepository.findAll()).isEmpty();
        sampleService.트랜잭션_메소드_내부에서_호출한_async_메소드에서_예외발생("test");
        List<Sample> samples = sampleRepository.findAll();

        assertThat(samples.size()).isEqualTo(1);
    }

    @Test
    void 메소드_내부에서_호출한_메소드가_async에서_예외가_발생해도_롤백이_발생하지_않는다() throws InterruptedException {
        assertThat(sampleRepository.findAll()).isEmpty();
        sampleService.메소드_내부에서_호출한_메소드가_async와_트랜잭션이_같이_있을때_예외발생();
        List<Sample> samples = sampleRepository.findAll();
        Thread.sleep(2000);

        assertThat(samples.size()).isEqualTo(1);
    }

    @Test
    void 메소드_내부에서_호출한_메소드가_async에서_예외가_발생해도_롤백이_발생하지_않는다2() throws InterruptedException {
        assertThat(sampleRepository.findAll()).isEmpty();
        sampleService.메소드_내부에서_호출한_메소드가_async와_트랜잭션이_같이_있을때_예외발생2();
        List<Sample> samples = sampleRepository.findAll();
        Thread.sleep(2000);

        assertThat(samples.size()).isEqualTo(1);
    }

    @Test
    void 메소드_내부에서_호출한_메소드가_async에서_예외가_발생해도_롤백이_발생하지_않는다3() throws InterruptedException {
        assertThat(sampleRepository.findAll()).isEmpty();
        sampleService.메소드_내부에서_호출한_메소드가_async만_있을_때_예외발생3();
        List<Sample> samples = sampleRepository.findAll();
        Thread.sleep(2000);

        assertThat(samples.size()).isEqualTo(2);
    }
}
