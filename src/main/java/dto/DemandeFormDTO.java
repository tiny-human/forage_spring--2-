package dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandeFormDTO {

    @NotBlank(message = "Le lieu est obligatoire")
    private String lieu;

    @NotNull(message = "L'identifiant du client est requis")
    private Long idClient;

    @NotNull(message = "La commune doit être spécifiée")
    private Long idCommune;

    @NotNull(message = "La date de demande est requise")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateDemande;
}