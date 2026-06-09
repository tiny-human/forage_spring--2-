package dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlerteDemandeResponseDTO {
    private DemandeResumeDTO demande;
    private StatutCourantDTO statutCourant;
    private List<AlerteParametreDTO> alertes;
    private List<AlerteParametreDTO> alertesToutes;
}
