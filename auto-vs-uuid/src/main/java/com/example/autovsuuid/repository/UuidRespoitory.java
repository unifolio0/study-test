package com.example.autovsuuid.repository;

import com.example.autovsuuid.domain.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UuidRespoitory extends JpaRepository<Uuid, String> {
}
