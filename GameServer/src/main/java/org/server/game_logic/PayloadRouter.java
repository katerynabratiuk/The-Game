package org.server.game_logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.lib.data.concurrency.ConcurrentQueue;
import org.lib.data.dto.ItemDTO;
import org.lib.data.payloads.*;
import org.lib.controllers.IRouter;
import org.lib.data.payloads.actors.PlayerCharacter;
import org.lib.data.payloads.actors.Coordinates;
import org.lib.data.payloads.game.Notification;
import org.lib.data.payloads.game.PlayerInput;
import org.lib.data.payloads.network.ConnectionRequest;
import org.lib.data.payloads.queries.*;
import org.lib.data.payloads.queries.search.CharacterFilterPayload;
import org.lib.packet_processing.send.SenderThread;
import org.server.db.model.Item;
import org.server.db.model.Pick;
import org.server.db.model.User;
import org.server.db.repository.criteria.CharacterSearchCriteria;
import org.server.db.model.*;
import org.server.db.repository.implementation.CharacterRepositoryImpl;
import org.server.db.repository.implementation.ItemRepositoryImpl;
import org.server.db.repository.implementation.PickRepositoryImpl;
import org.server.db.repository.implementation.UserRepositoryImpl;
import org.server.db.service.CharacterService;
import org.server.db.service.ItemService;
import org.server.db.service.PickService;
import org.server.db.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class PayloadRouter implements IRouter, Runnable {
    @Getter @Setter
    private SenderThread broadcastThread;
    @Getter @Setter
    private SenderThread unicastThread;

    private final ConcurrentQueue<NetworkPayload> receivedPackets = new ConcurrentQueue<>();
    private final GameStateManager gameStateManager;
    private UserService userService = new UserService(new UserRepositoryImpl());
    private PickService pickService = new PickService(new PickRepositoryImpl());
    private CharacterService characterService = new CharacterService(new CharacterRepositoryImpl());
    private ItemService itemService = new ItemService(new ItemRepositoryImpl());


    public PayloadRouter(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    @Override
    public void register(NetworkPayload payload) {
        receivedPackets.put(payload);
    }

    @Override
    public void run() {
        while (true) {
            try {
                NetworkPayload payload = receivedPackets.get();
                route(payload);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                break;
            }
        }
    }


    @SneakyThrows
    public void route(NetworkPayload payload) {
        for (var p: payload.getPayloads()) {
            switch (p.getType()) {
                case PLAYER_INPUT -> handlePlayerInput((PlayerInput) p);
                case NOTIFICATION -> handlePlayerNotification((Notification) p);
                case CONNECTION_REQUEST -> handleConnectionRequest((ConnectionRequest) p);
                case REGISTER -> handleRegister((RegisterPayload) p);
                case LOGIN -> handleLogin((LoginPayload) p);
                case CHARACTER_LIST -> handleCharacterList((CharacterListPayload)p);
                case CHARACTER_FILTER -> handleCharacterSearch((CharacterFilterPayload) p);
                case WEAPON_LIST -> handleWeaponList((WeaponListPayload)p);
                case POWERUP_LIST -> handlePowerupList((PowerUpListPayload)p);
                case PICK -> handlePick((UserPickPayload) p);
                default -> System.err.println("Unknown payload type: " + p);
            }
        }
    }

    private void handleWeaponList(WeaponListPayload query) {
        String clientUUID = query.getClientUUID();
        ItemService service = new ItemService(new ItemRepositoryImpl());

        var weapons = service.getItemsByType(Item.ItemType.WEAPON);
        var responsePayload = new WeaponListPayload(weapons);

        unicastThread.send(new NetworkPayload(List.of(responsePayload), clientUUID));
    }

    private void handlePowerupList(PowerUpListPayload query) {
        String clientUUID = query.getClientUUID();
        ItemService service = new ItemService(new ItemRepositoryImpl());


        List<ItemDTO> allItems = new ArrayList<>();
        allItems.addAll(service.getItemsByType(Item.ItemType.FLASH));
        allItems.addAll(service.getItemsByType(Item.ItemType.HEAL));
        allItems.addAll(service.getItemsByType(Item.ItemType.SPEED));

        var responsePayload = new PowerUpListPayload(allItems);

        unicastThread.send(new NetworkPayload(List.of(responsePayload), clientUUID));
    }


    private void handleCharacterList(CharacterListPayload query) {
        String clientUUID = query.getClientUUID();
        var service = new CharacterService(new CharacterRepositoryImpl());
        var characters = service.getAllCharacters();
        var responsePayload = new CharacterListPayload(characters);

        unicastThread.send(new NetworkPayload(List.of(responsePayload), clientUUID));
    }

    private void handleCharacterSearch(CharacterFilterPayload p) {

        var characters = characterService.filter(new CharacterSearchCriteria(p.getName(), p.getFast(), p.getArmor()));
        var responsePayload = new CharacterListPayload(characters);

        unicastThread.send(new NetworkPayload(List.of(responsePayload), p.getClientUUID()));
    }



    private void handleConnectionRequest(ConnectionRequest p) throws JsonProcessingException {
        switch (p.getConnectionCode()) {
            case JOIN:
                String username = gameStateManager.getUsernameByClientUUID(p.getClientUUID());
                var character = new PlayerCharacter(p.getClientUUID(), new Coordinates(0, 0), username);
                gameStateManager.addActor(character);
                var gameState = gameStateManager.snapshot();
                var networkPayload = new NetworkPayload(List.of(gameState));
                broadcastThread.send(networkPayload);
                break;

            case DISCONNECT:
                gameStateManager.removeActor(p.getClientUUID());
                broadcastThread.removeReceiver(p.getClientUUID());
        }
    }

    private void handlePlayerNotification(Notification notification) {
        System.out.println("handlePlayerNotification " + notification);
    }

    private void handleLogin(LoginPayload query) {
        System.out.println("Logged in " + query.getUsername());
        if (query.getClientUUID() != null && query.getUsername() != null) {
            if (userService.correctCredentials(new User(query.getUsername(), query.getPassword()))) {
                gameStateManager.registerUsername(query.getClientUUID(), query.getUsername());

                var successNotif = new Notification("Login successful");
                unicastThread.send(new NetworkPayload(List.of(successNotif), query.getClientUUID()));
            } else {
                var failNotif = new Notification("Incorrect credentials!");
                unicastThread.send(new NetworkPayload(List.of(failNotif), query.getClientUUID()));
            }
        }
    }


    public void handleRegister(RegisterPayload registerPayload) {
        String uuid = registerPayload.getClientUUID();
        String username = registerPayload.getUsername();
        String password = registerPayload.getPassword();

        if (uuid != null && username != null && password != null) {
            try {
                userService.registerUser(new User(username, password));
                gameStateManager.registerUsername(uuid, username);
                var notif = new Notification("Welcome to the map!");
                unicastThread.send(new NetworkPayload(List.of(notif), uuid));
            } catch (IllegalArgumentException e) {
                var notif = new Notification("Registration failed: " + e.getMessage());
                unicastThread.send(new NetworkPayload(List.of(notif), uuid));
            }
        } else {
            var notif = new Notification("Invalid registration data");
            unicastThread.send(new NetworkPayload(List.of(notif), uuid));
        }
    }

    private void handlePick(UserPickPayload pick) {
        String clientUUID = pick.getClientUUID();
        String username = gameStateManager.getUsernameByClientUUID(clientUUID);

        var character = characterService.getCharacter(pick.getCharacterId());
        var weapon = (Weapon) itemService.getItem(pick.getWeaponId());
        var powerUp = itemService.getItem(pick.getPowerUpId());

        if (character == null || weapon == null || powerUp == null) {
            var notif = new Notification("Invalid pick data received.");
            unicastThread.send(new NetworkPayload(List.of(notif), clientUUID));
            return;
        }

        var player = new PlayerCharacter(clientUUID, new Coordinates(0, 0), username);

        player.setMovementSpeed(player.getMovementSpeed() * character.getMovingSpeed());

        int newHp = player.getHitPoints() + character.getHeartPoints();
        player.setHitPoints(newHp);
        player.setMaxHp(newHp);
        player.setRateOfFire(weapon.getRof());
        player.setSprayAngle(weapon.getSpray());
        player.setDamage(weapon.getDamage());
        player.setImagePath(character.getImagePath());

        switch (powerUp.getType()) {
            case SPEED -> player.setMovementSpeed(player.getMovementSpeed() + 3);
            case HEAL -> player.setHitPoints(player.getHitPoints() + 2);
            case FLASH -> {} // TODO flash
        }

        gameStateManager.addActor(player);
        saveNewUserPick(character, weapon, powerUp);

        var notif = new Notification("Pick successful! Starting game...");
        unicastThread.send(new NetworkPayload(List.of(notif), clientUUID));
    }

    private void saveNewUserPick(GameCharacter character, Item weapon, Item powerUp) {
        var pick = new Pick();
        pick.setGameCharacter(character);
        pick.setWeapon(weapon);
        pick.setPowerUp(powerUp);
        pickService.createPick(pick);
    }

    private void handlePlayerInput(PlayerInput input) {
        if (broadcastThread == null) {
            System.out.println("Can`t process received message - sender thread is not initialized");
            return;
        }

        KeyBindingsHandler.updateKeyState(input.getClientUUID(), input.getKeyInputCode(), !input.isKeyReleased());
        var actor = gameStateManager.getPlayerCharacterByUUID(input.getClientUUID());
        if (actor == null) return;

        KeyBindingsHandler.processInput(actor, gameStateManager.getAllActors(), input);
        var gameState = gameStateManager.snapshot();
        broadcastThread.send(new NetworkPayload(List.of(gameState)));
    }
}
