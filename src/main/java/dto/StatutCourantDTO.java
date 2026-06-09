package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatutCourantDTO {
    private Long id;
    private String libelle;
    private String code;
    private String dateStatut;
    private String observations;
    private Long minutesEcoulees;
}
