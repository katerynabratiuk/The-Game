package org.lib.controllers;

import org.lib.data_structures.payloads.NetworkPayload;

public interface IRouter {
    void register(NetworkPayload payload);
    void handle(NetworkPayload payload);
}
