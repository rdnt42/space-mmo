package marowak.dev.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@MappedSuperclass
public class Equipment {
    @Id
    @GeneratedValue(generator = "equipment_seq")
    @Column(name = "equipment_id")
    Long id;

    int slotId;

    boolean equipped;
}
