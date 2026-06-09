package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "region") 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<District> districts;
}
