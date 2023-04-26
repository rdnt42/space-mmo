package marowak.dev.entity;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Entity
@Table(name = "characters")
public record Character(

        @Id
        String characterName,

        @NotBlank
        String accountName,

        @NotNull
        int experience,

        @NotNull
        double x,
        @NotNull
        double y,

        @NotNull
        int angle,

        @Column(name = "is_online")
        boolean online

        ) {

}
