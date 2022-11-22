package marowak.dev.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import marowak.dev.model.User;
import marowak.dev.repository.UserRepository;
import marowak.dev.request.AccountRequest;
import marowak.dev.response.AccountResponse;

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

    @Override
    public AccountResponse getAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("Account doesn't exists, username: " + username));

        return new AccountResponse(user.getUsername(), user.getEmail());
    }

    @Override
    public AccountResponse createAccount(AccountRequest request) {
        User user = User.builder()
                .username(request.username())
                .password(request.password())
                .email(request.email())
                .build();

        userRepository.save(user);

        return new AccountResponse(user.getUsername(), user.getEmail());
    }
}
