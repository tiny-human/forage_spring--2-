package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "adresse")
    private String adresse; 

}
