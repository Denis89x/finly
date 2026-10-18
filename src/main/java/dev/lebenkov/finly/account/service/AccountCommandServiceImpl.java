package dev.lebenkov.finly.account.service;

import dev.lebenkov.finly.account.dto.AccountRequest;
import dev.lebenkov.finly.account.entity.Account;
import dev.lebenkov.finly.account.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountCommandServiceImpl implements AccountCommandService {

    AccountRepository accountRepository;

    @Override
    public Account createAccount(AccountRequest accountRequest) {
        return null;
    }
}
