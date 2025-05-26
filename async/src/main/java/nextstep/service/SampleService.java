package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.domain.Sample;
import nextstep.repository.SampleRepository;
import nextstep.tool.AsyncProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;
    private final AsyncProvider asyncProvider;

    @Transactional
    public void 트랜잭션_메소드_내부에서_호출한_async_메소드에서_예외발생(String name) {
        sampleRepository.save(new Sample(name));
        asyncProvider.async_메소드_예외_발생();
    }

    @Transactional
    public void 메소드_내부에서_호출한_메소드가_async와_트랜잭션이_같이_있을때_예외발생() {
        sampleRepository.save(new Sample("example"));
        asyncProvider.async와_트랜잭션_같이_있는_메소드_저장_후_예외_발생();
    }

    @Transactional
    public void 메소드_내부에서_호출한_메소드가_async와_트랜잭션이_같이_있을때_예외발생2() {
        sampleRepository.save(new Sample("example"));
        asyncProvider.async와_트랜잭션_같이_있는_메소드_저장_후_예외_발생2();
    }

    @Transactional
    public void 메소드_내부에서_호출한_메소드가_async만_있을_때_예외발생3() {
        sampleRepository.save(new Sample("example"));
        asyncProvider.async와_트랜잭션_같이_있는_메소드_저장_후_예외_발생3();
    }
}
