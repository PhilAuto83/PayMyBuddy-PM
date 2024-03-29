package com.phildev.pmb.service;

import com.phildev.pmb.model.Connection;
import com.phildev.pmb.repository.ConnectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionService.class);

    @Autowired
    private ConnectionRepository connectionRepository;

    public Connection findByRecipientEmail(String senderEmail, String recipientEmail){
        Connection connectionFound = connectionRepository.getConnectionBySenderAndRecipientEmail(senderEmail, recipientEmail);
        if(connectionFound==null){
            logger.debug("No connection found for user email {}", senderEmail);
        }else{
            logger.info("Connection found for user {} and recipient {} with id {}", senderEmail, recipientEmail, connectionFound.getId());
        }
       return connectionRepository.getConnectionBySenderAndRecipientEmail(senderEmail, recipientEmail);
    }

    public List<Connection> findAllConnectionsFromCurrentUser(String email){
        List<Connection> connections = connectionRepository.getAllConnectionsFromUserEmail(email);
        if(connections.isEmpty()){
            logger.debug("No connection found");
        }
        return connections;
    }


    public Connection save(Connection connection){
        Connection connectionSaved = connectionRepository.save(connection);
        if(connectionRepository.getConnectionBySenderAndRecipientEmail(connection.getSenderEmail(), connection.getRecipientEmail()) == null){
            logger.error("An error occurred no connection was saved in database");
            throw new RuntimeException("An error occurred no connection was saved in database for connection with recipient email :"+connection.getRecipientEmail());
        }
        return connectionSaved;
    }

}
