package dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Getter
@Setter
public class DemandeStatutFormDTO {
    @NotNull(message = "Veuillez sélectionner une demande")
    private Long idDemande;

    @NotNull(message = "Veuillez entrer une date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;

    private String observation;
    
    @NotNull(message = "Veuillez sélectionner un statut")
    private Long idStatut;
}
