package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "demande_statut")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandeStatut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dm", nullable = false)
    private Demande demande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_statut", nullable = false)
    private Statut statut;

    @Column(name = "observation")
    private String observation;

    @Column(name = "date_statut", nullable = false)
    private java.time.LocalDateTime dateStatut;

    @Column(name = "dt")
    private Long dureeTravailleeMinutes;
}
