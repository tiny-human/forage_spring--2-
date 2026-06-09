package service;

import dto.DevisDetailDTO;
import dto.DevisFormDTO;
import model.*;
import repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DevisService {

    private final DevisRepository         devisRepo;
    private final DevisDetailRepository   devisDetailRepo;
    private final DemandeRepository       demandeRepo;
    private final TypeDevisRepository     typeRepo;
    private final StatutRepository        statutRepo;
    private final DemandeStatutService    demandeStatutService;

    public List<Devis> getDevisByDemande(Integer idDemande) {
        return devisRepo.findByDemandeId(Long.valueOf(idDemande));
    }

    public java.util.List<Devis> getAllDevis() {
        return devisRepo.findAllByOrderByDateDevisAscIdAsc();
    }

    public Devis getById(Long id) {
        return devisRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Devis introuvable : id=" + id));
    }

    @Transactional
    public Devis saveDevis(DevisFormDTO dto) {

        Demande   demande = demandeRepo.findById(dto.getIdDemande())
                .orElseThrow(() -> new RuntimeException("Demande introuvable : id=" + dto.getIdDemande()));
        TypeDevis type    = typeRepo.findById(dto.getIdType())
                .orElseThrow(() -> new RuntimeException("Type de devis introuvable : id=" + dto.getIdType()));

        // ── Étape 1 : sauvegarder le devis ───────────────────────
        Devis devis = new Devis();
        devis.setDemande(demande);
        devis.setDateDevis(dto.getDateDevis());
        devis.setType(type);
        devis = devisRepo.save(devis);

        // ── Étape 2 : sauvegarder les détails ────────────────────
        for (DevisDetailDTO ligneDto : dto.getDetails()) {
            DevisDetail detail = new DevisDetail();
            detail.setDevis(devis);
            detail.setMateriel(ligneDto.getLibelle());
            detail.setQuantite(ligneDto.getQuantite());
            detail.setPrixUnitaire(ligneDto.getPu());
            devisDetailRepo.save(detail);
        }

        // ── Étape 3 : construire le libellé du statut ─────────────
        // Convention BDD : "Devis <type.libelle> cree"
        // type.libelle est en minuscule ("etude" / "forage") → correspond exactement
        String libelleStatut = "Devis " + type.getLibelle().toLowerCase() + " cree";

        Statut statut = statutRepo.findByLibelleIgnoreCase(libelleStatut)
                .orElseThrow(() -> new RuntimeException(
                        "Statut introuvable : '" + libelleStatut +
                        "'. Vérifiez la table statut dans la base de données."));

        // ── Étape 4 : tracer le statut sur la demande ─────────────
        demandeStatutService.ajouterStatut(demande, statut, dto.getDateDevis(), dto.getObservations());

        return devis;
    }

    public List<DevisDetail> getDetailsByDevisId(Long idDevis) {
        return devisDetailRepo.findByDevisId(idDevis);
    }
}
