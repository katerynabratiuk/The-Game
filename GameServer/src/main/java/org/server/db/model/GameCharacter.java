package org.server.db.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GameCharacter {

    private Integer id;
    private String name;
    private String imagePath;
    private String description;
    private Double movingSpeed;
    private Integer heartPoints;

    public GameCharacter() {
    }

    public GameCharacter(Integer id, String name, String imagePath, String description, Double movingSpeed, Integer heartPoints) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
        this.movingSpeed = movingSpeed;
        this.heartPoints = heartPoints;
    }

}
