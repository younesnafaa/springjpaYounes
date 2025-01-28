package ssii.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ssii.entity.Participation;
import ssii.entity.Personne;
import ssii.entity.Projet;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {

    // Vérifie si une personne participe déjà à un projet
    boolean existsByPersonneAndProjet(Personne personne, Projet projet);

    // Trouve toutes les participations d'une personne
    List<Participation> findByPersonne(Personne personne);
}
