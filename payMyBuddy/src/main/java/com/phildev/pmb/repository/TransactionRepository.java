package com.phildev.pmb.repository;

import com.phildev.pmb.dto.TransactionDTO;
import com.phildev.pmb.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(
            value = "SELECT t.* FROM transaction t JOIN connection c on t.connection_id=c.id where c.sender_email=?1",
            nativeQuery = true
    )
    public List<Transaction> getTransactionsByEmail(String email);

    @Query(
            value = "SELECT new com.phildev.pmb.dto.TransactionDTO(u.firstName||' '||u.lastName, t.description, t.amount) FROM Transaction t JOIN Connection c on t.connectionId=c.id" +
                    " JOIN User u on c.recipientEmail=u.email where c.senderEmail=:email"
    )
    public Page<TransactionDTO> getTransactionsDTOByEmail(@Param("email")String email, Pageable pageable);


}
