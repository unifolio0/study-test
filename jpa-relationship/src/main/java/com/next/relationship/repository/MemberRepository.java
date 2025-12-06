package com.next.relationship.repository;

import com.next.relationship.domain.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT DISTINCT m FROM Member m JOIN FETCH m.orders")
    List<Member> findAllWithOrdersFetchJoin();
}
