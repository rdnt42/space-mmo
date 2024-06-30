package marowak.dev;

import io.micronaut.runtime.Micronaut;
import io.micronaut.serde.annotation.SerdeImport;
import message.CharacterMessage;
import message.ItemMessage;

@SerdeImport(ItemMessage.class)
@SerdeImport(CharacterMessage.class)
public class MechanicsApplication {

    public static void main(String[] args) {
        Micronaut.run(MechanicsApplication.class, args);
    }

}
