package ssii.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ssii.dao.PersonneRepository;
import ssii.dao.ProjetRepository;
import ssii.dao.ParticipationRepository;
import ssii.entity.Personne;
import ssii.entity.Projet;
import ssii.service.ParticipationService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ParticipationServiceTest {

    @Autowired
    private ParticipationService participationService;

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Test
    void enregistrerParticipation_PersonneDejaSurProjet_LanceException() {
        // Initialisation des données
        Personne personne = new Personne("Alice");
        personneRepository.save(personne);

        Projet projet = new Projet("Projet A", 5000);
        projetRepository.save(projet);

        participationService.enregistrerParticipation(personne.getMatricule(), projet.getCode(), "Développeur", 50);

        // Test : on tente d'ajouter une deuxième participation au même projet
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            participationService.enregistrerParticipation(personne.getMatricule(), projet.getCode(), "Développeur", 30);
        });

        assertEquals("La personne participe déjà à ce projet.", exception.getMessage());
    }

    @Test
    void enregistrerParticipation_DepassementDe100Pourcent_LanceException() {
        // Initialisation des données
        Personne personne = new Personne("Bob");
        personneRepository.save(personne);

        Projet projet1 = new Projet("Projet B", 3000);
        Projet projet2 = new Projet("Projet C", 4000);
        projetRepository.save(projet1);
        projetRepository.save(projet2);

        participationService.enregistrerParticipation(personne.getMatricule(), projet1.getCode(), "Chef de projet", 60);

        // Test : dépassement du pourcentage total
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            participationService.enregistrerParticipation(personne.getMatricule(), projet2.getCode(), "Développeur", 50);
        });

        assertEquals("La personne est occupée à plus de 100%.", exception.getMessage());
    }

    @Test
    void enregistrerParticipation_TropDeProjets_LanceException() {
        // Initialisation des données
        Personne personne = new Personne("Charlie");
        personneRepository.save(personne);

        Projet projet1 = new Projet("Projet D", 2000);
        Projet projet2 = new Projet("Projet E", 3000);
        Projet projet3 = new Projet("Projet F", 4000);
        Projet projet4 = new Projet("Projet G", 5000);
        projetRepository.save(projet1);
        projetRepository.save(projet2);
        projetRepository.save(projet3);
        projetRepository.save(projet4);

        participationService.enregistrerParticipation(personne.getMatricule(), projet1.getCode(), "Testeur", 20);
        participationService.enregistrerParticipation(personne.getMatricule(), projet2.getCode(), "Testeur", 30);
        participationService.enregistrerParticipation(personne.getMatricule(), projet3.getCode(), "Testeur", 40);

        // Test : ajout à un quatrième projet
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            participationService.enregistrerParticipation(personne.getMatricule(), projet4.getCode(), "Testeur", 10);
        });

        assertEquals("La personne ne peut pas participer à plus de 3 projets.", exception.getMessage());
    }
}
