package dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Getter
@Setter
public class ModifStatutDTO {
    private Long idDemande;
    private LocalDateTime date;
    private String observation;
    private Long idStatut;
}
