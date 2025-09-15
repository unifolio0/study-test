package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.entity.Test;
import nextstep.repository.TestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class InternalTransactionalService {

    private final TestRepository testRepository;

    @Transactional
    public void readOnlyFalseMethod() {
        testRepository.save(new Test("internal readOnly false"));
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        System.out.println("readOnly false 메서드 실행 결과 readOnly = " + isReadOnly);
    }

    @Transactional(readOnly = true)
    public void readOnlyTrueMethod() {
        testRepository.findAll();
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        System.out.println("readOnly true 메서드 실행 결과 readOnly = " + isReadOnly);
    }
}
