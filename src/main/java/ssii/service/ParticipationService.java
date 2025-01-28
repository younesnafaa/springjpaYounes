package ssii.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssii.dao.ParticipationRepository;
import ssii.dao.PersonneRepository;
import ssii.dao.ProjetRepository;
import ssii.entity.Participation;
import ssii.entity.Personne;
import ssii.entity.Projet;

@Service
public class ParticipationService {

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Transactional
    public void enregistrerParticipation(Integer matricule, Integer codeProjet, String role, Integer pourcentageTemps) {
        // Vérification des règles métier
        Personne personne = personneRepository.findById(matricule).orElseThrow(() -> new IllegalArgumentException("Personne non trouvée"));
        Projet projet = projetRepository.findById(codeProjet).orElseThrow(() -> new IllegalArgumentException("Projet non trouvé"));

        // Règle 1 : Une personne ne peut pas participer deux fois au même projet
        if (participationRepository.existsByPersonneAndProjet(personne, projet)) {
            throw new IllegalArgumentException("La personne participe déjà à ce projet.");
        }

        // Règle 2 : Une personne ne peut pas être occupée à plus de 100%
        int totalPourcentage = participationRepository.findByPersonne(personne).stream()
                .mapToInt(Participation::getPourcentageTemps).sum();
        if (totalPourcentage + pourcentageTemps > 100) {
            throw new IllegalArgumentException("La personne est occupée à plus de 100%.");
        }

        // Règle 3 : Une personne ne peut pas participer à plus de 3 projets
        long nombreDeProjets = participationRepository.findByPersonne(personne).stream().count();
        if (nombreDeProjets >= 3) {
            throw new IllegalArgumentException("La personne ne peut pas participer à plus de 3 projets.");
        }

        // Ajout de la participation
        Participation participation = new Participation(role, pourcentageTemps, personne, projet);
        participationRepository.save(participation);
    }
}
