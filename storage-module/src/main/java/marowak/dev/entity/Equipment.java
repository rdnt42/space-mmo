package marowak.dev.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Equipment {
    @Id
    @GeneratedValue(generator = "equipment_seq")
    @Column(name = "equipment_id")
    Long id;

    int slotId;

    boolean equipped;
}
