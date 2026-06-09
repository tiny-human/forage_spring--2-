package dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DevisFormDTO {

    // Identifiant de la demande liée (pré-rempli via @PathVariable, en lecture seule dans le form)
    @NotNull(message = "La demande est requise")
    private Long idDemande;

    @NotNull(message = "La date du devis est requise")
    private LocalDateTime dateDevis;

    // FK vers type_devis (Long, pas String) — le libellé sert seulement à construire le statut
    @NotNull(message = "Le type de devis est requis")
    private Long idType;

    private String observations;

    // @Valid propage la validation sur chaque DevisDetailDTO de la liste
    @NotEmpty(message = "Le devis doit contenir au moins un détail")
    @Valid
    private List<DevisDetailDTO> details = new ArrayList<>();

    // ── Calcul du montant total (non stocké, affiché à l'écran) ──
    public Double getMontantTotal() {
        if (details == null) return 0.0;
        return details.stream()
                .filter(d -> d.getQuantite() != null && d.getPu() != null)
                .mapToDouble(DevisDetailDTO::getTotal)
                .sum();
    }
}