package model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "devis_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class DevisDetail {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_devis", nullable = false)
    private Devis devis;

    @Column(name = "materiel", nullable = false)
    private String materiel;

    @Column(name = "quantite")
    private Double quantite;
    
    @Column(name = "prix_unitaire")
    private Double prixUnitaire;
    
    @Transient // Ignore cette colonne dans la base de données
    public Double getSousTotal() {
        return (quantite != null && prixUnitaire != null) ? quantite * prixUnitaire : 0.0;
    }
}
