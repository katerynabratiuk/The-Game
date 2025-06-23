package org.server.db.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Item {
    private Integer id;
    private String name;
}
