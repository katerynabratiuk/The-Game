package org.client.game_logic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPickDTO {
    private Integer characterId;
    private Integer weaponId;
    private Integer powerUpId;
}
