package com.example.autovsuuid.repository;

import com.example.autovsuuid.domain.Auto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoReposiory extends JpaRepository<Auto, Long> {
}
