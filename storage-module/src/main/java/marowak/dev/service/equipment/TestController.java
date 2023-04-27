package marowak.dev.service.equipment;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.AllArgsConstructor;
import marowak.dev.entity.Equipment;

import java.util.List;

@AllArgsConstructor
@Controller("/equipment")
public class TestController {
    private final EquipmentService equipmentService;

    @Get
    public List<Equipment> index() {
        return equipmentService.getAllOnline();
    }
}
