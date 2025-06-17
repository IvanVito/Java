package game.datasource.records.repository;

import game.datasource.records.model.Records;
import org.springframework.data.repository.CrudRepository;

public interface RecordsRepository extends CrudRepository<Records, String> {
}
