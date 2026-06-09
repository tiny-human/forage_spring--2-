package service;

import dto.*;
import model.*;
import repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandeStatutService {

    private final DemandeRepository     demandeRepo;
    private final StatutRepository      statutRepo;
    private final DemandeStatutRepository demandeStatutRepo;

    public List<DemandeStatut> getAllDemandeStatuts() {
        return demandeStatutRepo.findAll();
    }

    public DemandeStatut getById(Long demandeId) {
        return demandeStatutRepo.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable : id=" + demandeId));
    }

        @Transactional
        public DemandeStatut ajouterStatut(Demande demande, Statut statut, LocalDateTime dateStatut, String observation) {
        LocalDateTime dateEffective = dateStatut != null ? dateStatut : LocalDateTime.now();
        DemandeStatut precedent = demandeStatutRepo.findTopByDemandeIdOrderByDateStatutDescIdDesc(demande.getId())
            .orElse(null);

        DemandeStatut ds = new DemandeStatut();
        ds.setDemande(demande);
        ds.setStatut(statut);
        ds.setObservation(observation);
        ds.setDateStatut(dateEffective);
        ds.setDureeTravailleeMinutes(precedent != null
            ? Math.max(0L, ChronoUnit.MINUTES.between(precedent.getDateStatut(), dateEffective))
            : null);
        DemandeStatut saved = demandeStatutRepo.save(ds);
        recalculerDureeTravaillee(demande.getId());
        return saved;
        }

    
    @Transactional
    public void saveDemandeSTatut(DemandeStatutFormDTO dto) {
        Demande demande = demandeRepo.findById(dto.getIdDemande())
                .orElseThrow(() -> new RuntimeException("Demande introuvable : id=" + dto.getIdDemande()));
        Statut statut = statutRepo.findById(dto.getIdStatut())
                .orElseThrow(() -> new RuntimeException("Statut introuvable : id=" + dto.getIdStatut()));
        ajouterStatut(demande, statut, dto.getDate(), dto.getObservation());
    }

    @Transactional
    public void updateDemandeStatut(ModifStatutDTO dto){
        Demande demande = demandeRepo.findById(dto.getIdDemande())
                .orElseThrow(() -> new RuntimeException("Demande introuvable : id=" + dto.getIdDemande()));
        Statut statut = statutRepo.findById(dto.getIdStatut())
                .orElseThrow(() -> new RuntimeException("Statut introuvable : id=" + dto.getIdStatut()));
        LocalDateTime dateStatut = dto.getDate() != null ? dto.getDate() : LocalDateTime.now();

        DemandeStatut aModifier = demandeStatutRepo
                .findTopByDemandeIdAndStatutIdOrderByDateStatutDescIdDesc(demande.getId(), statut.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Aucun enregistrement trouvé pour la demande id=" + demande.getId() + " et le statut id=" + statut.getId()));

        aModifier.setDemande(demande);
        aModifier.setStatut(statut);
        aModifier.setObservation(dto.getObservation());
        aModifier.setDateStatut(dateStatut);
        demandeStatutRepo.save(aModifier);

        recalculerDureeTravaillee(demande.getId());
    }

    private void recalculerDureeTravaillee(Long idDemande) {
        List<DemandeStatut> historiqueAsc = new ArrayList<>(
                demandeStatutRepo.findByDemandeIdOrderByDateStatutAscIdAsc(idDemande));

        DemandeStatut precedent = null;
        for (DemandeStatut courant : historiqueAsc) {
            if (precedent == null) {
                courant.setDureeTravailleeMinutes(null);
            } else {
                long minutes = ChronoUnit.MINUTES.between(precedent.getDateStatut(), courant.getDateStatut());
                courant.setDureeTravailleeMinutes(Math.max(0L, minutes));
            }
            precedent = courant;
        }
        demandeStatutRepo.saveAll(historiqueAsc);
    }
}