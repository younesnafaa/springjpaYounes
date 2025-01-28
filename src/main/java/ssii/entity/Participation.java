package ssii.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Participation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String role; // Rôle dans le projet

    @NonNull
    private Integer pourcentageTemps; // % de temps alloué au projet

    @ManyToOne
    @NonNull
    private Personne personne; // Relation avec Personne

    @ManyToOne
    @NonNull
    private Projet projet; // Relation avec Projet
}
