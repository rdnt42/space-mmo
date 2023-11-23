package marowak.dev.service.command;

import reactor.core.CorePublisher;

public interface CharacterCommand<T> {
    CorePublisher<T> execute(String characterName);
}
