package marowak.dev.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import marowak.dev.request.AccountRequest;
import marowak.dev.response.AccountResponse;
import marowak.dev.service.AccountService;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 22.11.2022
 * Time: 23:33
 */
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/accounts")
public class AccountController {
    @Inject
    private AccountService accountService;

    @Post
    public AccountResponse create(AccountRequest request) {
        return accountService.createAccount(request);
    }

    @Get("/{username}")
    public AccountResponse get(@PathVariable String username) {
        return accountService.getAccount(username);
    }
}
