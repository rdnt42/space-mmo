package marowak.dev.service;

import marowak.dev.request.AccountRequest;
import marowak.dev.response.AccountResponse;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 21.11.2022
 * Time: 23:08
 */
public interface AccountService {
    AccountResponse getAccount(String username);

    AccountResponse createAccount(AccountRequest request);
}
