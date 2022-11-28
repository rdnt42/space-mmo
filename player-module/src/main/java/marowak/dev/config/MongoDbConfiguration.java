package marowak.dev.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.naming.Named;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 28.11.2022
 * Time: 22:07
 */
@ConfigurationProperties("player-character-db")
public interface MongoDbConfiguration extends Named {

    @NonNull
    String getCollection();
}
