package org.server;

import org.junit.jupiter.api.Test;
import org.server.DatabaseAccess.SamplePostgresDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseTests {
    @Test
    public void testDBConnection() {
        SamplePostgresDriver.TestConnection();
        assertTrue(true);
    }
}
