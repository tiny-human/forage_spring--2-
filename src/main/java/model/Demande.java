package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "demande")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @Column(name = "lieu", nullable = false)
    private String lieu;

    @Column(name = "date_demande", nullable = false)
    private LocalDateTime dateDemande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_commune", nullable = false)
    private Commune commune;

    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<DemandeStatut> demandeStatuts;

    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Devis> devis;
}
