package com.phildev.pmb.repository;


import com.phildev.pmb.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Integer> {

    @Query(
        value = "SELECT * FROM connection c WHERE c.sender_email = ?1 AND c.recipient_email = ?2",
        nativeQuery = true
    )
    public Connection getConnectionBySenderAndRecipientEmail(String sender_email, String recipient_email);

    @Query(
            value = "SELECT * FROM connection c WHERE c.sender_email = ?1",
            nativeQuery = true
    )
    public List<Connection> getAllConnectionsFromUserEmail(String email);

    


}
