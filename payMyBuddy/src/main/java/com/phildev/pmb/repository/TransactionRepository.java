package com.phildev.pmb.repository;

import com.phildev.pmb.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(
            value = "SELECT t.* FROM transaction t JOIN connection c on t.connection_id=c.id where c.sender_email=?1",
            nativeQuery = true
    )
    public List<Transaction> getTransactionsByEmail(String email);


}
