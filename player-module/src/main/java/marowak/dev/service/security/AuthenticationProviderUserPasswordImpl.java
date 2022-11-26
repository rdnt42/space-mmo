package marowak.dev.service.security;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import marowak.dev.model.User;
import marowak.dev.repository.UserRepository;
import org.reactivestreams.Publisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 25.11.2022
 * Time: 0:04
 */
@Singleton
public class AuthenticationProviderUserPasswordImpl implements AuthenticationProvider {
    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flux.create(emitter -> {
            String username = (String) authenticationRequest.getIdentity();
            String password = (String) authenticationRequest.getSecret();
            boolean isValid = isValidUser(username, password);

            if (isValid) {
                emitter.next(AuthenticationResponse.success((String) authenticationRequest.getIdentity()));
                emitter.complete();
            } else {
                emitter.error(AuthenticationResponse.exception("Login failed"));
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }

    private boolean isValidUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("User with name" + username + "doesn't exists"));

        return passwordEncoder.matches(password, user.getPassword());
    }
}
