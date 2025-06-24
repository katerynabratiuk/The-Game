package org.server.db.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String username;

    public UserDTO(String username) {
        this.username = username;
    }

}
