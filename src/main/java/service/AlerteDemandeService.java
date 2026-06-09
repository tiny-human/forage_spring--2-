package service;

import dto.AlerteParametreDTO;
import model.Demande;
import model.DemandeStatut;
import model.Parametre;
import repository.DemandeRepository;
import repository.DemandeStatutRepository;
import repository.ParametreRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlerteDemandeService {

    private final DemandeRepository demandeRepo;
    private final DemandeStatutRepository demandeStatutRepo;
    private final ParametreRepository parametreRepo;

    @Transactional(readOnly = true)
    public List<AlerteParametreDTO> getAlertesPourDemande(String demandeParam) {
        Demande demande = rechercherDemande(demandeParam);

        List<DemandeStatut> historique = new ArrayList<>(
                demandeStatutRepo.findByDemandeIdOrderByDateStatutAscIdAsc(demande.getId()));

        List<AlerteParametreDTO> alertes = new ArrayList<>();
        if (historique.size() < 2) {
            return alertes;
        }

        List<Parametre> parametres = parametreRepo.findAll();
        for (Parametre parametre : parametres) {
            if (parametre.getStatutSource() == null || parametre.getStatutCible() == null) {
                continue;
            }

            DemandeStatut source = trouverPremiereOccurrence(historique, parametre.getStatutSource().getId());
            if (source == null) {
                continue;
            }

            DemandeStatut cible = trouverPremiereOccurrenceApres(historique, parametre.getStatutCible().getId(),
                    source.getDateStatut(), source.getId());
            if (cible == null) {
                continue;
            }

            long minutesEcoulees = ChronoUnit.MINUTES.between(source.getDateStatut(), cible.getDateStatut());
            long duree = parametre.getDuree() != null ? parametre.getDuree() : 0L;
            if (minutesEcoulees < duree) {
                continue;
            }

            String statutSource = parametre.getStatutSource().getLibelle();
            String statutCible = parametre.getStatutCible().getLibelle();
            String message = String.format(
                    "La transition de \"%s\" vers \"%s\" a pris %d minute(s) et déclenche une alerte %s.",
                    statutSource,
                    statutCible,
                    minutesEcoulees,
                    parametre.getCouleur());

            alertes.add(new AlerteParametreDTO(
                    parametre.getId(),
                    parametre.getCouleur(),
                    statutSource,
                    statutCible,
                    duree,
                    minutesEcoulees,
                    Math.max(0L, duree - minutesEcoulees),
                    true,
                    message));
        }

        return alertes;
    }

    private DemandeStatut trouverPremiereOccurrence(List<DemandeStatut> historique, Long idStatut) {
        for (DemandeStatut ds : historique) {
            if (ds.getStatut() != null && idStatut.equals(ds.getStatut().getId())) {
                return ds;
            }
        }
        return null;
    }

    private DemandeStatut trouverPremiereOccurrenceApres(List<DemandeStatut> historique, Long idStatut,
            LocalDateTime apresDate, Long apresId) {
        for (DemandeStatut ds : historique) {
            if (ds.getStatut() == null || !idStatut.equals(ds.getStatut().getId())) {
                continue;
            }
            if (ds.getDateStatut() == null) {
                continue;
            }
            boolean estApres = ds.getDateStatut().isAfter(apresDate)
                    || (ds.getDateStatut().isEqual(apresDate) && ds.getId() != null && ds.getId() > apresId);
            if (estApres) {
                return ds;
            }
        }
        return null;
    }

    private Demande rechercherDemande(String demandeParam) {
        String valeur = demandeParam == null ? "" : demandeParam.trim();
        if (valeur.isEmpty()) {
            throw new RuntimeException("Paramètre demande obligatoire.");
        }

        if (valeur.chars().allMatch(Character::isDigit)) {
            Long id = Long.valueOf(valeur);
            return demandeRepo.findByIdWithDetails(id)
                    .orElseThrow(() -> new RuntimeException("Demande introuvable : id=" + id));
        }

        return demandeRepo.findByReferenceWithDetails(valeur)
                .orElseThrow(() -> new RuntimeException("Demande introuvable : référence=" + valeur));
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAlertesToutes() {
        List<Demande> demandes = demandeRepo.findAllWithDetails();
        List<Map<String, Object>> resultat = new ArrayList<>();

        for (Demande demande : demandes) {
            // Statut courant
            String statutCourant = demandeStatutRepo
                    .findTopByDemandeIdOrderByDateStatutDescIdDesc(demande.getId())
                    .map(ds -> ds.getStatut().getLibelle())
                    .orElse(null);

            List<AlerteParametreDTO> alertes = getAlertesPourDemande(
                    String.valueOf(demande.getId()));

            Map<String, Object> bloc = new LinkedHashMap<>();
            bloc.put("id", demande.getId());
            bloc.put("reference", demande.getReference());
            bloc.put("client", demande.getClient() != null ? demande.getClient().getNom() : null);
            bloc.put("statutCourant", statutCourant);
            bloc.put("alertes", alertes);
            resultat.add(bloc);
        }
        return resultat;
    }
}
