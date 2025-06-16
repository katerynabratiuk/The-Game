package org.lib.GameControllers;

public interface IController<T> {
    void register(T event);
    void handle(T event);
}
