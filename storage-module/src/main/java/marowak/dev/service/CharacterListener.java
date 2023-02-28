package marowak.dev.service;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import marowak.dev.request.CharacterRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

@RequiredArgsConstructor
@KafkaListener(batch = true)
public class CharacterListener {
    private final CharacterCommandService characterCommandService;

    @Topic("characters")
    public<T> void receive(List<ConsumerRecord<String, CharacterRequest>> records) {
//        return null;
//        return characterCommandService.executeCommand(key.get(0), request);
    }

//    @Topic("characters")
//    public<T> void receive(List<ConsumerRecord<String, String>> records) {
////        return null;
////        return characterCommandService.executeCommand(key.get(0), request);
//    }
}
