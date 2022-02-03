package com.sparta.board.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {

    @Id
    private String id;

    @Column(nullable = false)
    private String value;

    public RefreshToken updateValue(String token){
        this.value = value;
        return this;
    }

    @Builder
    public RefreshToken(String id, String value){
        this.value = value;
        this.id = id;
    }
}
