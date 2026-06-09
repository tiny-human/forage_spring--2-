package dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DevisDetailDTO {

    @NotBlank(message = "Le libellé est obligatoire")
    private String libelle;

    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private Double quantite;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @Positive(message = "Le prix unitaire doit être positif")
    private Double pu;

    // Calculé côté serveur si besoin — non stocké en base
    @Transient  // pas de Transient JPA ici (c'est un DTO), on le laisse comme méthode
    public Double getTotal() {
        return (quantite != null && pu != null) ? quantite * pu : 0.0;
    }
}
