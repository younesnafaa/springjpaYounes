package ssii.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ssii.entity.Personne;

public interface PersonneRepository extends JpaRepository<Personne, Integer> {
}
