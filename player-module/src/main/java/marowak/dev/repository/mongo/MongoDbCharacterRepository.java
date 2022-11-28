package marowak.dev.repository.mongo;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.config.MongoDbConfiguration;
import marowak.dev.model.mongo.Character;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 28.11.2022
 * Time: 22:12
 */
@RequiredArgsConstructor
@Singleton
public class MongoDbCharacterRepository implements CharacterRepository {
    private final MongoDbConfiguration configuration;
    private final MongoClient mongoClient;

    @Override
    @NotNull
    public Publisher<Character> list() {
        return getCollection().find();
    }

    @Override
    @NotNull
    public Mono<Boolean> save(@NonNull @NotNull @Valid Character character) {
        return Mono.from(getCollection().insertOne(character))
                .map(insertOneResult -> true)
                .onErrorReturn(false);
    }

    private MongoCollection<Character> getCollection() {
        return mongoClient.getDatabase(configuration.getName())
                .getCollection(configuration.getCollection(), Character.class);
    }
}
