package org.client.UI;

public interface InputCallback {
    void onKeyPressed(int keyCode);
    void onKeyReleased(int keyCode);
    void onMouseClicked(int x, int y);
}
