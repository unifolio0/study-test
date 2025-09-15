package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.entity.Test;
import nextstep.repository.TestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExternalTransactionalService {

    private final InternalTransactionalService internalTransactionalService;
    private final TestRepository testRepository;

    @Transactional
    public void readOnlyFalseInvokeReadOnlyFalseMethod() {
        testRepository.save(new Test("external readOnly false"));
        System.out.println("readOnly false에서 readOnly false를 호출");
        internalTransactionalService.readOnlyFalseMethod();
    }

    @Transactional
    public void readOnlyFalseInvokeReadOnlyTrueMethod() {
        testRepository.save(new Test("external readOnly false"));
        System.out.println("readOnly false에서 readOnly true를 호출");
        internalTransactionalService.readOnlyTrueMethod();
    }

    @Transactional(readOnly = true)
    public void readOnlyTrueInvokeReadOnlyFalseMethod() {
        testRepository.findAll();
        System.out.println("readOnly true에서 readOnly false를 호출");
        internalTransactionalService.readOnlyFalseMethod();
    }

    @Transactional(readOnly = true)
    public void readOnlyTrueInvokeReadOnlyTrueMethod() {
        testRepository.findAll();
        System.out.println("readOnly true에서 readOnly true를 호출");
        internalTransactionalService.readOnlyTrueMethod();
    }
}
