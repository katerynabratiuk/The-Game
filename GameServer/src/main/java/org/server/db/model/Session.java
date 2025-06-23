package org.server.db.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Session {


    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private User user;
    private Pick pick;
    private Integer killCount;

    public Session() {
    }

    public Session(Integer id, User user, Pick pick) {
        this.id = id;
        this.user = user;
        this.pick = pick;
        this.killCount = 0;
    }


}
