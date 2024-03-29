package com.phildev.pmb.repository;

import com.phildev.pmb.model.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

    @Query(
            value = "SELECT ac.balance FROM account ac JOIN user u ON ac.id=u.id where u.email=?1",
            nativeQuery = true
    )
    double getBalanceByUserEmail(String email);


    @Modifying
    @Transactional
    @Query(
            value = "UPDATE account ac JOIN user u ON ac.id=u.id SET ac.balance=?1  where u.email=?2",
            nativeQuery = true
    )
    public void updateAccountBalance(double amount, String email);
}
