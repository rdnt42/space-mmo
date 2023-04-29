package marowak.dev.service.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import message.CharacterMessage;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.BaseSubscriber;
import reactor.util.annotation.NonNull;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterCommandService {
    private final CharacterService characterService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void tryExecuteCommand(CharacterMessageKey key, byte[] bytes) {
        try {
            executeCommand(key, bytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // TODO fix this
    ObjectMapper mapper = new ObjectMapper();

    private void executeCommand(CharacterMessageKey key, byte[] bytes) throws IOException {
        CharacterMessage message = objectMapper.readValue(bytes, CharacterMessage.class);
        Publisher<CharacterMessage> publisher = switch (key) {
            case CHARACTER_CREATE -> characterService.create(message);
            case CHARACTER_MOTION_UPDATE -> characterService.updateMotion(message);
            case CHARACTER_STATE_UPDATE -> characterService.updateState((message));
        };

        Subscriber<CharacterMessage> subscriber = getSubscriber(key);
        publisher.subscribe(subscriber);
    }

    // FIXME
    private Subscriber<CharacterMessage> getSubscriber(CharacterMessageKey key) {
        String fixme = "fixme pls";
        return new BaseSubscriber<>() {
            @Override
            protected void hookOnComplete() {
                super.hookOnComplete();
                log.debug("Received command: {}, for account:{}, character: {}",
                        key, fixme, fixme);
            }

            @Override
            protected void hookOnError(@NonNull Throwable throwable) {
                super.hookOnError(throwable);
                log.error("Error when command: {}, for account:{}, character: {}, error: {}",
                        key, fixme, fixme, throwable);
            }
        };
    }
}
