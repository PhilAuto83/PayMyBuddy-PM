package com.phildev.pmb.service;


import com.phildev.pmb.model.Connection;
import com.phildev.pmb.repository.ConnectionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest
public class ConnectionServiceTest {

    @Autowired
    ConnectionService connectionService;

    @Test
    public void testUserIsFoundByEmail(){
        Connection connection = connectionService.findByRecipientEmail("test.pmb@test.fr","testee.money@test.fr");
        Assertions.assertEquals("testee.money@test.fr", connection.getRecipientEmail());
        Assertions.assertEquals("test.pmb@test.fr", connection.getSenderEmail());
    }

    @Test
    public void testConnectionNotFoundByRecipientEmail(){
        Connection connection = connectionService.findByRecipientEmail("testee.money@test.fr","test.pmb@test.fr");
        Assertions.assertNull(connection);
    }

    @Test
    public void testAllConnectionsAreRetrievedForSenderEmail(){
        List<Connection> connections = connectionService.findAllConnectionsFromCurrentUser("test.pmb@test.fr");
        Assertions.assertEquals(1, connections.size());
        Assertions.assertEquals("testee.money@test.fr", connections.getFirst().getRecipientEmail());
    }

    @Test
    public void testNullObjectForSenderEmail(){
        List<Connection> connections = connectionService.findAllConnectionsFromCurrentUser("testee.money@test.fr");
        Assertions.assertEquals(Collections.EMPTY_LIST, connections);
    }

    @Test
    public void testSavingConnection(){
        Connection connection = new Connection("tester.cousy@test.fr","testee.money@test.fr");
        Connection savedConnection = connectionService.save(connection);
        Assertions.assertEquals("tester.cousy@test.fr",savedConnection.getSenderEmail());
        Assertions.assertEquals("testee.money@test.fr",savedConnection.getRecipientEmail());
    }
}
