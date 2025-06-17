package game.domain.user.userInterface;

import game.datasource.user.model.UserData;
import game.domain.user.model.SignUpRequest;

import java.util.Optional;
import java.util.UUID;

public interface UserInterface {
    boolean registration (SignUpRequest request);
    UUID authenticate(String authorizationHeader);
    Optional<UserData> authorize(String uuid);
}
