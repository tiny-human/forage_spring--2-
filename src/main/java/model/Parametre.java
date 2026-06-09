package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parametre")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parametre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_statut1", nullable = false)
    private Statut statutSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_statut2", nullable = false)
    private Statut statutCible;

    @Column(name = "debut", nullable = false)
    private Long debut;

    @Column(name = "fin", nullable = false)
    private Long fin;

    @Column(name = "couleur")
    private String couleur;
}
