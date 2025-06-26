package org.lib.controllers;

import org.lib.data.payloads.NetworkPayload;

public interface IRouter {
    void register(NetworkPayload payload);
    void route(NetworkPayload payload);
}
