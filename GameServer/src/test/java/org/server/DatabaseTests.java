package org.server;

import org.junit.jupiter.api.Test;
import org.server.db.database_access.DbConnection;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseTests {
    @Test
    public void testDBConnection() {
        DbConnection.getConnection();
        assertTrue(true);
    }
}
