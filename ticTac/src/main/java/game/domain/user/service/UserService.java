package game.domain.user.service;

import game.datasource.user.model.UserData;
import game.domain.user.userInterface.UserInterface;
import game.domain.user.model.SignUpRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import java.util.Optional;
import java.util.UUID;

public class UserService implements UserInterface {
    private final game.datasource.user.repository.UserRepository userRepository;

    public UserService(game.datasource.user.repository.UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean registration(SignUpRequest request) {
        if (userRepository.findByLogin(request.getLogin()).isPresent())
            return false;

        String hashedPassword = Base64.getEncoder().encodeToString((request.getLogin() + ":" + request.getPassword()).getBytes(StandardCharsets.UTF_8));
        UserData user = new UserData(UUID.randomUUID(), request.getLogin(), hashedPassword);
        userRepository.save(user);

        return true;
    }

    @Override
    public UUID authenticate(String authorizationHeader) {
        byte[] decodedBytes = Base64.getDecoder().decode(authorizationHeader);
        String decodedString = new String(decodedBytes);
        String[] loginAndPass = decodedString.split(":");
        String login = loginAndPass[0];
        UserData user = userRepository.findByLogin(login).orElse(null);

        if(user != null && user.getPassword().equals(authorizationHeader))
            return user.getUuid();
        else
            return null;
    }

    @Override
    public Optional<UserData> authorize(String uuid) {
        return userRepository.findByUuid(UUID.fromString(uuid));
    }
}

