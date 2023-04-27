package marowak.dev.service.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Character;
import marowak.dev.enums.CharacterMessageKey;
import marowak.dev.request.message.CharacterMotionRequest;
import marowak.dev.request.message.CharacterRequest;
import marowak.dev.request.message.CharacterStateRequest;
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

    public void tryExecuteCommand(CharacterMessageKey key, byte[] bytes)  {
        try {
            executeCommand(key, bytes);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void executeCommand(CharacterMessageKey key, byte[] bytes) throws IOException {
        CharacterRequest request = convertToRequest(key, bytes);
        Publisher<Character> publisher = switch (key) {
            case CHARACTER_CREATE -> characterService.create((CharacterMotionRequest)request);
            case CHARACTER_MOTION_UPDATE -> characterService.updateMotion((CharacterMotionRequest)request);
            case CHARACTER_STATE_UPDATE -> characterService.updateState((CharacterStateRequest)request);
        };

        Subscriber<Character> subscriber = getSubscriber(key, request);
        publisher.subscribe(subscriber);
    }

    private CharacterRequest convertToRequest(CharacterMessageKey key, byte[] request) throws IOException {
        return switch (key) {
            case CHARACTER_CREATE, CHARACTER_MOTION_UPDATE -> objectMapper.readValue(request, CharacterMotionRequest.class);
            case CHARACTER_STATE_UPDATE -> objectMapper.readValue(request, CharacterStateRequest.class);
        };
    }

    private Subscriber<Character> getSubscriber(CharacterMessageKey key, CharacterRequest request) {
        return new BaseSubscriber<>() {
            @Override
            protected void hookOnComplete() {
                super.hookOnComplete();
                log.debug("Received command: {}, for account:{}, character: {}",
                        key, request.characterName(), request.characterName());
            }

            @Override
            protected void hookOnError(@NonNull Throwable throwable) {
                super.hookOnError(throwable);
                log.error("Error when command: {}, for account:{}, character: {}, error: {}",
                        key, request.accountName(), request.characterName(), throwable);
            }
        };
    }
}
