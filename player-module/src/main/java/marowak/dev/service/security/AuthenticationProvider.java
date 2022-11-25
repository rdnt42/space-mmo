package marowak.dev.service.security;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import org.reactivestreams.Publisher;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 25.11.2022
 * Time: 0:00
 */
public interface AuthenticationProvider {
    Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest);
}
