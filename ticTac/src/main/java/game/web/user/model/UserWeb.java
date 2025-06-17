package game.web.user.model;

import java.util.UUID;

public class UserWeb {
    private UUID uuid;
    private String name;
    private String password;

    public UserWeb(UUID uuid, String name, String password) {
        this.uuid = uuid;
        this.name = name;
        this.password = password;
    }

    public UserWeb() {
        uuid = UUID.randomUUID();
        name = "";
        password = "";
    }

    public UUID getUuid() {
        return uuid;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
};
