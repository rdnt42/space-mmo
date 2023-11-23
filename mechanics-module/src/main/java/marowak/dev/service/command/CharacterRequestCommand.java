package marowak.dev.service.command;

import reactor.core.CorePublisher;

public interface CharacterRequestCommand<R, T> {
    CorePublisher<T> execute(R request, String characterName);
}
