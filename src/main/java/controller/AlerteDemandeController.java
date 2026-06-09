package controller;

import dto.AlerteParametreDTO;
import lombok.RequiredArgsConstructor;
import model.Demande;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.AlerteDemandeService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/demandes")
@RequiredArgsConstructor
public class AlerteDemandeController {

    private final AlerteDemandeService alerteDemandeService;

    @GetMapping("/alertes")
    public ResponseEntity<Map<String, Object>> alertesParQuery(@RequestParam("demande") String demande) {
        return ResponseEntity.ok(reponseOk(alerteDemandeService.getAlertesPourDemande(demande)));
    }

    @GetMapping("/{demande}/alertes")
    public ResponseEntity<Map<String, Object>> alertesParPath(@PathVariable String demande) {
        return ResponseEntity.ok(reponseOk(alerteDemandeService.getAlertesPourDemande(demande)));
    }

    @GetMapping("/toutes-alertes")
    public ResponseEntity<Map<String, Object>> toutesAlertes() {
        Map<String, Object> reponse = new LinkedHashMap<>();
        reponse.put("ok", true);
        reponse.put("demandes", alerteDemandeService.getAlertesToutes());
        return ResponseEntity.ok(reponse);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listeDemandes() {
        return ResponseEntity.ok(alerteDemandeService.getAlertesToutes()
                .stream()
                .map(d -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("id", d.get("id"));
                    item.put("reference", d.get("reference"));
                    return item;
                })
                .collect(java.util.stream.Collectors.toList()));
    }

    private Map<String, Object> reponseOk(List<AlerteParametreDTO> alertes) {
        Map<String, Object> reponse = new LinkedHashMap<>();
        reponse.put("ok", true);
        reponse.put("alertes", alertes);
        return reponse;
    }

}
