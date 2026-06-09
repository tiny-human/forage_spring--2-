package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "devis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Devis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_demande", nullable = false)
    private Demande demande;

    @Column(name = "date_devis")
    private LocalDateTime dateDevis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type", nullable = false)
    private TypeDevis type;

    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<DevisDetail> details = new java.util.ArrayList<>();

    @Transient
    public Double getPrixTotal() {
        return details.stream().mapToDouble(DevisDetail::getSousTotal).sum();
    }
}
