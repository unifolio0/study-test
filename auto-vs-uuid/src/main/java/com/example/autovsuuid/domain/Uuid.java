package com.example.autovsuuid.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Uuid {

    @Id
    private String id;

    private String value;

    public Uuid(String value) {
        this.id = UUID.randomUUID().toString();
        this.value = value;
    }
}
