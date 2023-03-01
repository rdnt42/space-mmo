package marowak.dev.service;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Character;
import marowak.dev.enums.CharacterMessageKey;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.BaseSubscriber;
import reactor.util.annotation.NonNull;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@KafkaListener(batch = true)
public class CharacterListener {
    private final CharacterCommandService characterCommandService;

    @Topic("characters")
    public void receive(@KafkaKey List<CharacterMessageKey> keys, List<CharacterRequest> requests) {
        for (int i = 0; i < requests.size(); i++) {
            CharacterMessageKey key = keys.get(i);
            CharacterRequest request = requests.get(i);
            Publisher<Character> publisher = characterCommandService.executeCommand(key, request);

            Subscriber<Character> subscriber = getSubscriber(key, request);
            publisher.subscribe(subscriber);
        }
    }

    private Subscriber<Character> getSubscriber(CharacterMessageKey key, CharacterRequest request) {
        return new BaseSubscriber<>() {
            @Override
            protected void hookOnComplete() {
                super.hookOnComplete();
                log.debug("Received command: {}, for account:{}, character: {}",
                        key, request.accountName(), request.characterName());
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
