package marowak.dev.service.character;

import message.CharacterMessage;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

//@KafkaClient(batch = true)
public interface CharactersUpdateClient {

    //    @Topic("characters")
    Flux<RecordMetadata> sendCharacters(Collection<CharacterMessage> messages);

    //    @Topic("characters")
    Mono<RecordMetadata> sendCharacter(CharacterMessage message);

}
