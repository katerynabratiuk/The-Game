package org.lib.data_structures.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CharacterDTO {
    private Integer id;
    private String name;
    private String description;
    private String imagePath;
}
