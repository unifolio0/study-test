package com.nextstep.repository;

import com.nextstep.domain.DynamicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DynamicEntityRepository extends JpaRepository<DynamicEntity, Long> {
}
