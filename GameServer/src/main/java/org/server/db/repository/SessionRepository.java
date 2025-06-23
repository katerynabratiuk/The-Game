package org.server.db.repository;

import org.server.db.model.Session;
import org.server.db.model.User;

import java.util.List;

public interface SessionRepository extends GenericRepository<Session,Integer>{


    void addEndTime(Session session);
    void addKillCount(Session session);
    List<Session> getUsersSessions(User user);
}
