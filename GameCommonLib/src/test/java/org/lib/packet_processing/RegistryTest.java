package org.lib.packet_processing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lib.packet_processing.registry.IReceiverRegistryObserver;
import org.lib.packet_processing.registry.SocketAddressRegistry;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import static org.junit.jupiter.api.Assertions.*;

public class RegistryTest {
    private SocketAddressRegistry registry;
    private SocketAddress address0;

    static class TestObserver implements IReceiverRegistryObserver {
        boolean notified = false;
        @Override
        public void onReceiverAdded() {
            notified = true;
        }
    }

    @BeforeEach
    void setup() {
        registry = new SocketAddressRegistry();
        address0 = new InetSocketAddress("localhost", 5687);
    }

    @Test
    void testAddAndGet() {
        registry.add("client", address0);
        assertEquals(address0, registry.get("client"));
    }

    @Test
    void testRemove() {
        registry.add("client", address0);
        registry.remove("client");
        assertNull(registry.get("client"));
    }

    @Test
    void testObserverNotifiedOnFirstReceiverAdded() {
        var observer = new TestObserver();
        registry.addObserver(observer);

        registry.add("client", address0);
        assertTrue(observer.notified);
    }
}
