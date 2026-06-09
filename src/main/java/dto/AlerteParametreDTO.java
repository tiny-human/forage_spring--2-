package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlerteParametreDTO {
    private Long parametreId;
    private String couleur;
    private String statutSource;
    private String statutCible;
    private Long dureeMinutes;
    private Long minutesEcoulees;
    private Long minutesRestantes;
    private Boolean declenchee;
    private String message;
}
