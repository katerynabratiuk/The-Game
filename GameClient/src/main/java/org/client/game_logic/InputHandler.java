package org.client.game_logic;

import org.client.UI.InputCallback;
import org.client.UI.MapPanel;
import org.client.network.PacketsSenderService;
import org.lib.data.payloads.actors.Coordinates;
import org.lib.data.payloads.game.PlayerInput;
import org.lib.data.payloads.game.Vector;

import java.awt.event.MouseEvent;

public class InputHandler implements InputCallback {
    private final PacketsSenderService packetsSenderService;

    public InputHandler(MapPanel mapPanel, PacketsSenderService packetsSenderService) {
        this.packetsSenderService = packetsSenderService;
        mapPanel.setInputCallback(this);
    }

    @Override
    public void onKeyPressed(int keyCode) {
        PlayerInput input = new PlayerInput(packetsSenderService.getClientId(), keyCode);
        packetsSenderService.sendInput(input);
    }

    @Override
    public void onKeyReleased(int keyCode) {
        PlayerInput input = new PlayerInput(packetsSenderService.getClientId(), keyCode);
        input.setKeyReleased(true);
        packetsSenderService.sendInput(input);
    }

    @Override
    public void onMouseClicked(int x, int y) {
        var direction = new Vector(new Coordinates(x, y));
        PlayerInput input = new PlayerInput(packetsSenderService.getClientId(), MouseEvent.BUTTON1);
        input.setDirection(direction);

        System.out.println("Mouse clicked at: " + input.getDirection().getX() + " " + input.getDirection().getY());
        packetsSenderService.sendInput(input);
    }
}
