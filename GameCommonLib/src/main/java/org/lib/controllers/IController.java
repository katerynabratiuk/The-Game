package org.lib.controllers;

import org.lib.data_structures.payloads.NetworkPayload;

public interface IController {
    void register(NetworkPayload payload);
}
