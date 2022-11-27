package marowak.dev.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import lombok.RequiredArgsConstructor;
import marowak.dev.request.AccountRequest;
import marowak.dev.response.AccountResponse;
import marowak.dev.service.AccountService;

import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 22.11.2022
 * Time: 23:33
 */
@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
@Validated
@Controller("/accounts")
public class AccountController {
    private final AccountService accountService;

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post
    public AccountResponse create(@Valid AccountRequest request) {
        return accountService.createAccount(request);
    }

    @Get("/{username}")
    public AccountResponse get(@PathVariable String username) {
        return accountService.getAccount(username);
    }
}
