package dev.lebenkov.finly.account.repository;

import dev.lebenkov.finly.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUser_Id(Long id);
}
