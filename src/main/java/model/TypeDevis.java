package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "type_devis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypeDevis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle", nullable = false)
    private String libelle;
}
