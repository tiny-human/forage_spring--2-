package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "commune")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Commune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_district", nullable = false)
    private District district;
}
