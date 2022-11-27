package marowak.dev.service;

import marowak.dev.model.User;
import marowak.dev.repository.UserRepository;
import marowak.dev.request.AccountRequest;
import marowak.dev.response.AccountResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 27.11.2022
 * Time: 21:10
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void getAccountNotExists() {
        String username = "user";
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                accountService.getAccount(username));
        assertEquals("Account doesn't exists, username: " + username, exception.getMessage());
    }

    @Test
    void getAccountSuccess() {
        String username = "user";
        User user = getDefaultUser(username
        );
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        AccountResponse response = accountService.getAccount(username);
        assertEquals(user.getUsername(), response.userName());
        assertEquals(user.getEmail(), response.email());
    }

    @Test
    void createAccountBusyUsername() {
        String username = "user";
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User()));
        AccountRequest request = getFilledRequest(username);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                accountService.createAccount(request));

        assertEquals("This username is already taken", exception.getMessage());
    }

    @Test
    void createAccountBusyEmail() {
        String username = "user";
        AccountRequest request = getFilledRequest(username);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(new User()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                accountService.createAccount(request));

        assertEquals("This email address is already taken", exception.getMessage());
    }

    @Test
    void createAccountSuccess() {
        String username = "user";
        AccountRequest request = getFilledRequest(username);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password()))
                .thenReturn("encodedPass");

        AccountResponse response = accountService.createAccount(request);

        assertEquals(username, response.userName());
        assertEquals(request.email(), response.email());
    }

    private User getDefaultUser(String username) {
        return User.builder()
                .username(username)
                .password("pass")
                .email("email")
                .build();
    }

    private AccountRequest getFilledRequest(String username) {
        return new AccountRequest(username, "pass", "email");
    }
}