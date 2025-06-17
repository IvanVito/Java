package game.datasource.user.repository;

import game.datasource.user.model.UserData;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserData, String> {
    Optional<UserData> findByLogin(String login);
    Optional<UserData> findByUuid(UUID uuid);
}
