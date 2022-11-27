package marowak.dev.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import marowak.dev.model.User;
import marowak.dev.repository.UserRepository;
import marowak.dev.request.AccountRequest;
import marowak.dev.response.AccountResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 21.11.2022
 * Time: 23:12
 */
@Singleton
public class AccountServiceImpl implements AccountService {
    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Override
    public AccountResponse getAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("Account doesn't exists, username: " + username));

        return new AccountResponse(user.getUsername(), user.getEmail());
    }

    @Override
    public AccountResponse createAccount(AccountRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("This username is already taken");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("This email address is already taken");
        }

        String encodePass = passwordEncoder.encode(request.password());
        User user = User.builder()
                .username(request.username())
                .password(encodePass)
                .email(request.email())
                .build();

        userRepository.save(user);

        return new AccountResponse(user.getUsername(), user.getEmail());
    }
}
