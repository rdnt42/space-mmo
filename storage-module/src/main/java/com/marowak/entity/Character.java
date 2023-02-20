package com.marowak.entity;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@MappedEntity
public record Character(

        @Id
        String characterName,

        @NotBlank
        String accountName,

        @NotNull
        int experience,

        @NotNull
        int x,
        @NotNull
        int y,

        @NotNull
        int angle
        ) {
}
