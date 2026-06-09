package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandeResumeDTO {
    private Long id;
    private String reference;
    private String clientNom;
    private String telephone;
    private String clientAdresse;
    private String communeNom;
    private String lieu;
    private String dateDemande;
}
