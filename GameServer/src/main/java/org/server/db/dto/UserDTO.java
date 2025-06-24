package org.server.db.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String username;
    private String country;

    public UserDTO(String username, String country) {
        this.username = username;
        this.country = country;
    }

}
