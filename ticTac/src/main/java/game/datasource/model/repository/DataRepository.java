package game.datasource.model.repository;

import game.datasource.model.DataGame;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataRepository extends CrudRepository<DataGame, String> {
    List<DataGame> findByGameOver(boolean f);
}