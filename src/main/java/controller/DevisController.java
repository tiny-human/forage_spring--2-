package controller;

import dto.DevisDetailDTO;
import dto.DevisFormDTO;
import repository.TypeDevisRepository;
import service.DemandeService;
import service.DevisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import model.Devis;
import model.Demande;
import model.DemandeStatut;
import model.DevisDetail;
import repository.DemandeStatutRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/devis")
@RequiredArgsConstructor
public class DevisController {

    private final DevisService devisService;
    private final DemandeService demandeService;
    private final TypeDevisRepository typeRepo;
    private final repository.DemandeStatutRepository demandeStatutRepo;

    @GetMapping("/nouveau/{idDemande}")
    public String showForm(@PathVariable Long idDemande, Model model) {

        DevisFormDTO form = new DevisFormDTO();
        form.setIdDemande(idDemande);
        form.setDateDevis(LocalDateTime.now());

        // Ajouter une première ligne vide pour que le tableau s'affiche dès le départ
        form.getDetails().add(new DevisDetailDTO());

        model.addAttribute("devisForm", form);
        model.addAttribute("demande", demandeService.getById(idDemande));
        model.addAttribute("types", typeRepo.findAll());
        return "devis/form";
    }

    /**
     * Transaction interne au service : 1. save(Devis) 2. save(DevisDetail) pour
     * chaque ligne 3. find(Statut) par libellé construit dynamiquement 4.
     * save(DemandeStatut)
     */
    @PostMapping
    public String save(
            @Valid @ModelAttribute("devisForm") DevisFormDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes flash) {

        if (result.hasErrors()) {
            model.addAttribute("demande", demandeService.getById(dto.getIdDemande()));
            model.addAttribute("types", typeRepo.findAll());
            return "devis/form";
        }

        try {
            devisService.saveDevis(dto);
            flash.addFlashAttribute("success", "Devis enregistré avec succès !");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("demande", demandeService.getById(dto.getIdDemande()));
            model.addAttribute("types", typeRepo.findAll());
            return "devis/form";
        }

        return "redirect:/demandes";
    }

    @GetMapping
    public String list(Model model) {
        var devis = devisService.getAllDevis();
        model.addAttribute("devisList", devis);

        java.util.Map<Long, List<DemandeStatut>> historiqueStatuts = new java.util.HashMap<>();

        devis.stream()
                .map(d -> d.getDemande())
                .distinct()
                .forEach(dm -> {
                    List<DemandeStatut> historiques = demandeStatutRepo.findByDemandeIdOrderByDateStatutAscIdAsc(dm.getId());

                    historiqueStatuts.put(dm.getId(), historiques);
                });

        model.addAttribute("historiqueStatuts", historiqueStatuts);
        return "devis/list";
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        List<DevisDetail> dt = devisService.getDetailsByDevisId(id);
        model.addAttribute("devisDetail", dt);
        return "devis/details";
    }
}
