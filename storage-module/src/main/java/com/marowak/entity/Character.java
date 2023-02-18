package com.marowak.entity;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@MappedEntity
public record Character(
        @GeneratedValue
        @Id
        Long id,

        @NotBlank
        String characterName,

        @NotBlank
        String accountName,

        @NotNull
        Integer experience
        ) {
}
