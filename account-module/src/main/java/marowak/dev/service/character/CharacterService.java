package marowak.dev.service.character;

import marowak.dev.request.CharacterRequest;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 28.11.2022
 * Time: 21:55
 */
public interface CharacterService {
    void createCharacter(CharacterRequest request, String username);
}
