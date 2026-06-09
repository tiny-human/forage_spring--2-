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
    private Long idDemande;
    private String couleur;
    private String statutSource;
    private String statutCible;
    private Long debut;
    private Long fin;
    private Long minutesEcoulees;
    private Long minutesRestantes;
    private Long dureeTotaleMinutes;
    private Boolean declenchee;
}
