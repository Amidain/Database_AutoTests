package DTO;

import java.time.LocalDateTime;

public class Session {
    private long id;
    private String session_key;
    private LocalDateTime created_time;
    private int build_number;

    public Session(String session_key, int build_number) {
        this.session_key = session_key;
        this.created_time = LocalDateTime.now();
        this.build_number = build_number;
    }

    public Session(long id, String session_key,LocalDateTime created_time, int build_number) {
        this.id = id;
        this.session_key = session_key;
        this.created_time = created_time;
        this.build_number = build_number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public LocalDateTime getCreated_time() {
        return created_time;
    }

    public void setCreated_time(LocalDateTime created_time) {
        this.created_time = created_time;
    }

    public int getBuild_number() {
        return build_number;
    }

    public void setBuild_number(int build_number) {
        this.build_number = build_number;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", session_key=" + session_key +
                ", created_time=" + created_time +
                ", build_number=" + build_number +
                '}';
    }
}
