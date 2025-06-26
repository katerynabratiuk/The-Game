package org.server.gamelogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lib.data.payloads.NetworkPayload;
import org.lib.data.payloads.enums.ConnectionCode;
import org.lib.data.payloads.game.GameState;
import org.lib.data.payloads.game.Notification;
import org.lib.data.payloads.network.ConnectionRequest;
import org.lib.data.payloads.queries.LoginPayload;
import org.lib.data.payloads.queries.UserPickPayload;
import org.lib.data.payloads.queries.search.CharacterFilterPayload;
import org.lib.data.payloads.queries.search.WeaponFilterPayload;
import org.lib.packet_processing.send.SenderThread;
import org.server.db.service.CharacterService;
import org.server.db.service.ItemService;
import org.server.db.service.UserService;
import org.server.game_logic.GameStateManager;
import org.server.game_logic.PayloadRouter;

import java.util.List;
import static org.mockito.Mockito.*;

public class PayloadRouterTest {

    private GameStateManager gameStateManager;
    private PayloadRouter router;
    private SenderThread unicastThread;
    private SenderThread broadcastThread;

    @BeforeEach
    public void setUp() {
        gameStateManager = mock(GameStateManager.class);
        unicastThread = mock(SenderThread.class);
        broadcastThread = mock(SenderThread.class);

        router = new PayloadRouter(gameStateManager);
        router.setUnicastThread(unicastThread);
        router.setBroadcastThread(broadcastThread);
    }

    @Test
    public void testHandleLoginWithCorrectCredentials() {
        var payload = new LoginPayload();
        payload.setClientUUID("123123");
        payload.setUsername("test");
        payload.setPassword("test");

        var mockUserService = mock(UserService.class);
        when(mockUserService.correctCredentials(any())).thenReturn(true);
        router = new PayloadRouter(gameStateManager);
        router.setUnicastThread(unicastThread);
        router.setBroadcastThread(broadcastThread);

        router.route(new NetworkPayload(List.of(payload)));

        verify(unicastThread, times(1)).send(any(NetworkPayload.class));
    }

    @Test
    public void testHandleLoginWithIncorrectCredentials() {
        LoginPayload payload = new LoginPayload();
        payload.setClientUUID("client");
        payload.setUsername("incorrect");
        payload.setPassword("incorrect");

        UserService mockUserService = mock(UserService.class);
        when(mockUserService.correctCredentials(any())).thenReturn(false);
        router.setUserService(mockUserService);

        router.route(new NetworkPayload(List.of(payload)));

        verify(unicastThread).send(argThat(networkPayload -> {
            Object arg = networkPayload.getPayloads().get(0);
            return arg instanceof Notification &&
                    ((Notification) arg).getMessage().contains("Incorrect credentials");
        }));
    }

    @Test
    public void testHandleCharacterSearchSendsResponse() {
        var mockCharacterService = mock(CharacterService.class);
        router.setCharacterService(mockCharacterService);

        var payload = new CharacterFilterPayload();
        payload.setClientUUID("client");
        payload.setName("name");

        when(mockCharacterService.filter(any())).thenReturn(List.of());

        router.route(new NetworkPayload(List.of(payload)));

        verify(unicastThread).send(any(NetworkPayload.class));
    }

    @Test
    public void testHandleWeaponFilterSendsFilteredWeapons() {
        var mockItemService = mock(ItemService.class);
        router.setItemService(mockItemService);
        var payload = new WeaponFilterPayload();
        payload.setClientUUID("client");

        when(mockItemService.filter(any())).thenReturn(List.of());

        router.route(new NetworkPayload(List.of(payload)));

        verify(unicastThread).send(any(NetworkPayload.class));
    }

    @Test
    public void testHandleConnectionJoinAddsActorAndBroadcasts() throws Exception {
        ConnectionRequest request = new ConnectionRequest();
        request.setClientUUID("uuid");
        request.setConnectionCode(ConnectionCode.JOIN);

        when(gameStateManager.getUsernameByClientUUID("uuid")).thenReturn("player1");
        when(gameStateManager.snapshot()).thenReturn(new GameState());

        router.route(new NetworkPayload(List.of(request)));

        verify(gameStateManager).addActor(any());
        verify(broadcastThread).send(any(NetworkPayload.class));
    }

    @Test
    public void testHandlePickWithInvalidDataSendsErrorNotification() {
        UserPickPayload payload = new UserPickPayload();
        payload.setClientUUID("uuid");
        payload.setCharacterId(1);
        payload.setWeaponId(2);
        payload.setPowerUpId(3);

        router.setCharacterService(mock(CharacterService.class));
        router.setItemService(mock(ItemService.class));

        when(router.getCharacterService().getCharacter(1)).thenReturn(null);

        router.route(new NetworkPayload(List.of(payload)));

        verify(unicastThread).send(argThat(p ->
                p.getPayloads().stream().anyMatch(np -> np instanceof Notification notif &&
                        notif.getMessage().contains("Invalid pick"))
        ));
    }
}