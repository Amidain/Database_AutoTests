package DAO;

import DTO.Session;

import java.util.List;

public interface ISessionDAO {

    Session getSessionById(int id);
    List<Session> getAllSessions();
    boolean addSession(Session session);
    boolean updateSession(Session session);
    boolean deleteSession(int id);
}
