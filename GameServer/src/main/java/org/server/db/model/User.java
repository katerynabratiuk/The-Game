package org.server.db.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {

    private String username;
    private String password;
    private List<Session> sessions;


    public User(String username, String password) {
        this.password = password;
        this.username = username;
    }

    public User() {
    }

    public void addSession(Session session)
    {
        this.sessions.add(session);
    }

}
