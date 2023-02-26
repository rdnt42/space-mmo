package marowak.dev.service.character;

import marowak.dev.model.mongo.Character;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 28.11.2022
 * Time: 21:55
 */
public interface CharacterService {
    Publisher<Character> getCharacters(String username);

    void createCharacter(CharacterRequest request, String username);
}
