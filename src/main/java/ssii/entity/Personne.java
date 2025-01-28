package ssii.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-génère l'ID
    private Integer matricule;

    @NonNull
    private String nom; // Nom de la personne
}
