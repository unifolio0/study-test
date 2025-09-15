package com.nextstep;

import static org.assertj.core.api.Assertions.assertThat;

import com.nextstep.domain.DynamicEntity;
import com.nextstep.domain.NotDynamicEntity;
import com.nextstep.repository.DynamicEntityRepository;
import com.nextstep.repository.NotDynamicEntityRepository;
import com.nextstep.service.DirtyCheckingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ExtendWith(DataBaseCleaner.class)
class DirtyCheckingTest {

    @Autowired
    private NotDynamicEntityRepository notDynamicEntityRepository;

    @Autowired
    private DynamicEntityRepository dynamicEntityRepository;

    @Autowired
    private DirtyCheckingService dirtyCheckingService;

    @Test
    void 더티체킹을_했을때_필드별로_sql이_나갈까_전체가_나갈까() {
        Long id = dirtyCheckingService.saveNotDynamicDummyData();
        dirtyCheckingService.updateNotDynamicColumn1(id, "changedColumn1");
        NotDynamicEntity notDynamicEntity = notDynamicEntityRepository.findById(id).orElseThrow();

        assertThat(notDynamicEntity.getColumn1()).isEqualTo("changedColumn1");
    }

    @Test
    void DynamicUpdate_어노테이션을_붙인_엔티티는_변경된_필드만_sql이_나간다() {
        Long id = dirtyCheckingService.saveDynamicDummyData();
        dirtyCheckingService.updateDynamicColumn1(id, "changedColumn1");
        DynamicEntity dirtyChecking = dynamicEntityRepository.findById(id).orElseThrow();

        assertThat(dirtyChecking.getColumn1()).isEqualTo("changedColumn1");
    }
}
