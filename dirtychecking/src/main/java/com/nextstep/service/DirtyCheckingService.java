package com.nextstep.service;

import com.nextstep.domain.DynamicEntity;
import com.nextstep.domain.NotDynamicEntity;
import com.nextstep.repository.DynamicEntityRepository;
import com.nextstep.repository.NotDynamicEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DirtyCheckingService {

    private final NotDynamicEntityRepository notDynamicEntityRepository;
    private final DynamicEntityRepository dynamicEntityRepository;

    @Transactional
    public Long saveNotDynamicDummyData() {
        NotDynamicEntity saved = notDynamicEntityRepository.save(new NotDynamicEntity(
                "column1",
                "column2",
                "column3",
                "column4",
                "column5",
                "column6",
                "column7",
                "column8",
                "column9",
                "column10"
        ));
        return saved.getId();
    }

    @Transactional
    public Long saveDynamicDummyData() {
        DynamicEntity saved = dynamicEntityRepository.save(new DynamicEntity(
                "column1",
                "column2",
                "column3",
                "column4",
                "column5",
                "column6",
                "column7",
                "column8",
                "column9",
                "column10"
        ));
        return saved.getId();
    }

    @Transactional
    public void updateNotDynamicColumn1(Long id, String newValue) {
        NotDynamicEntity entity = notDynamicEntityRepository.findById(id).orElseThrow();
        entity.setColumn1(newValue);
    }

    @Transactional
    public void updateDynamicColumn1(Long id, String newValue) {
        DynamicEntity entity = dynamicEntityRepository.findById(id).orElseThrow();
        entity.setColumn1(newValue);
    }
}
