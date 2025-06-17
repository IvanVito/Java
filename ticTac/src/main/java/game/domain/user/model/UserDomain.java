package game.domain.user.model;

import java.util.UUID;

public class UserDomain {
        private UUID uuid;
        private String login;
        private String password;

        public UserDomain(UUID uuid, String login, String password) {
            this.uuid = uuid;
            this.login = login;
            this.password = password;
        }

        public UserDomain() {

        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    };
