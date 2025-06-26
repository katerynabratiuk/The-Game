package org.client.gamelogic;

import org.client.UI.InventoryPanel;
import org.client.UI.MapPanel;
import org.client.game_logic.PayloadRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lib.data.payloads.actors.Inventory;
import org.lib.data.payloads.game.GameState;

import javax.swing.SwingUtilities;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PayloadRouterTest {

    private PayloadRouter router;
    private MapPanel mapPanel;

    @BeforeEach
    void setUp() {
        mapPanel = mock(MapPanel.class);
        InventoryPanel inventoryPanel = mock(InventoryPanel.class);
        when(mapPanel.getInventoryPanel()).thenReturn(inventoryPanel);

        router = new PayloadRouter(mapPanel);
    }

    @Test
    void testInvokeHandleInventoryViaReflection() throws Exception {
        Inventory inventory = mock(Inventory.class);

        var handleInventory = PayloadRouter.class.getDeclaredMethod("handleInventory", Inventory.class);
        handleInventory.setAccessible(true);

        mockStatic(SwingUtilities.class).when(() -> SwingUtilities.invokeLater(any()))
                .thenAnswer(invocation -> {
                    Runnable r = invocation.getArgument(0);
                    r.run();
                    return null;
                });

        handleInventory.invoke(router, inventory);
        verify(mapPanel.getInventoryPanel(), times(1)).updateInventory(inventory);
    }

    @Test
    void testInvokeHandleGameStateViaReflection() throws Exception {
        var gameState = mock(GameState.class);
        var handleGameState = PayloadRouter.class.getDeclaredMethod("handleGameState", GameState.class);
        handleGameState.setAccessible(true);

        handleGameState.invoke(router, gameState);

        verify(mapPanel, times(1)).updateGameState(gameState);
    }
}