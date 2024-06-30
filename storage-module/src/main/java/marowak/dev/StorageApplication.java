package marowak.dev;

import io.micronaut.runtime.Micronaut;
import io.micronaut.serde.annotation.SerdeImport;
import message.CharacterMessage;
import message.ItemMessage;

@SerdeImport(ItemMessage.class)
@SerdeImport(CharacterMessage.class)
public class StorageApplication {

    public static void main(String[] args) {
        Micronaut.run(StorageApplication.class, args);
    }
}