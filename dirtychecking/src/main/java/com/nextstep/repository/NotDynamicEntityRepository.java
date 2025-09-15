package com.nextstep.repository;

import com.nextstep.domain.NotDynamicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotDynamicEntityRepository extends JpaRepository<NotDynamicEntity, Long> {
}
