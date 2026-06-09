package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "district")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Commune> communes;
}
