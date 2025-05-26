package nextstep.tool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.domain.Sample;
import nextstep.repository.SampleRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncProvider {

    private final SampleRepository sampleRepository;

    @Async
    public void async_메소드_예외_발생() {
        throw new IllegalArgumentException("async 동작 실패");
    }

    @Async
    @Transactional
    public void async와_트랜잭션_같이_있는_메소드_저장_후_예외_발생() {
        sampleRepository.save(new Sample("name"));
        log.info("async 내부 저장 완료");
        throw new IllegalArgumentException("async 동작 실패");
    }

    @Transactional
    @Async
    public void async와_트랜잭션_같이_있는_메소드_저장_후_예외_발생2() {
        sampleRepository.save(new Sample("name"));
        log.info("async 내부 저장 완료");
        throw new IllegalArgumentException("async 동작 실패");
    }

    @Async
    public void async와_트랜잭션_같이_있는_메소드_저장_후_예외_발생3() {
        sampleRepository.save(new Sample("name"));
        throw new IllegalArgumentException("async 동작 실패");
    }
}
