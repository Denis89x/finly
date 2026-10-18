package dev.lebenkov.finly.account.service;

import dev.lebenkov.finly.account.dto.AccountRequest;
import dev.lebenkov.finly.account.entity.Account;

public interface AccountCommandService {

    Account createAccount(AccountRequest accountRequest);
}
