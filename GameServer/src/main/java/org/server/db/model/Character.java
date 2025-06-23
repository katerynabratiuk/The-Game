package org.server.db.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Character {

    private Integer id;
    private String name;
    private String imagePath;
    private String description;
    private Double movingSpeed;
    private Integer heartPoints;

    public Character() {
    }

    public Character(Integer id, String name, String imagePath, String description, Double movingSpeed, Integer heartPoints) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
        this.movingSpeed = movingSpeed;
        this.heartPoints = heartPoints;
    }

}
