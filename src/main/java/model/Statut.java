package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "statut")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Statut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "code", nullable = false, unique = true)
    private String code;
}