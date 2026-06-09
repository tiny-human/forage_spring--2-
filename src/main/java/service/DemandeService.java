package service;

import dto.DemandeFormDTO;
import model.*;
import repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandeService {

    private final DemandeRepository     demandeRepo;
    private final ClientRepository      clientRepo;
    private final CommuneRepository     communeRepo;
    private final StatutRepository      statutRepo;
    private final DemandeStatutRepository demandeStatutRepo;

    public List<Demande> getAllDemandes() {
        return demandeRepo.findAllWithDetails();
    }

    public Demande getById(Long id) {
        return demandeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande introuvable : id=" + id));
    }

    @Transactional
    public void saveDemande(DemandeFormDTO dto) {
        Client  client  = clientRepo.findById(dto.getIdClient())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));
        Commune commune = communeRepo.findById(dto.getIdCommune())
                .orElseThrow(() -> new RuntimeException("Commune introuvable"));

        Demande demande = new Demande();
        demande.setReference(genererReference());
        demande.setClient(client);
        demande.setLieu(dto.getLieu());
        demande.setCommune(commune);
        demande.setDateDemande(dto.getDateDemande()); 
        
        demande = demandeRepo.save(demande);

        Statut statutInitial = statutRepo.findByLibelle("Demande creee")
                .orElseThrow(() -> new RuntimeException("Statut 'Demande creee' introuvable en base."));

        DemandeStatut ds = new DemandeStatut();
        ds.setDemande(demande);
        ds.setStatut(statutInitial);
        ds.setDateStatut(LocalDateTime.now());
        demandeStatutRepo.save(ds);
    }

    private String genererReference() {
        long count = demandeRepo.count() + 1; // count() est plus standard
        return String.format("DE%03d", count);
    }
}