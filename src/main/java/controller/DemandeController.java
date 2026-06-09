package controller;

import dto.DemandeFormDTO;
import repository.*;
import model.Demande;
import service.DemandeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/demandes")
@RequiredArgsConstructor
public class DemandeController {

    private final DemandeService demandeService;
    private final ClientRepository clientRepo;
    private final CommuneRepository communeRepo;
    private final DemandeStatutRepository demandeStatutRepo;

  
    @GetMapping
    public String list(Model model) {
        // Stocker la liste pour éviter un double appel au service/base de données
        var demandes = demandeService.getAllDemandes();
        model.addAttribute("demandes", demandes);

        // Dernier statut de chaque demande pour affichage dans le tableau
        // Clé = idDemande, valeur = libellé du statut courant
        java.util.Map<Long, String> statutCourant = new java.util.HashMap<>();
        demandes.forEach(d -> demandeStatutRepo.findTopByDemandeIdOrderByDateStatutDescIdDesc(d.getId())
            .ifPresent(ds -> statutCourant.put(d.getId(), ds.getStatut().getLibelle())));
        model.addAttribute("statutCourant", statutCourant);
        return "demande/list"; 
    }

    // formulaire
    @GetMapping("/nouveau")
    public String showForm(Model model) {
        model.addAttribute("demandeForm", new DemandeFormDTO());
        chargerListes(model);
        return "demande/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("demandeForm") DemandeFormDTO dto,
            BindingResult result, Model model, RedirectAttributes flash) {

        if (result.hasErrors()) {
            System.out.println("Erreurs de validation : " + result.getAllErrors());
            chargerListes(model);
            return "demande/form";
        }

        try {
            demandeService.saveDemande(dto);
            flash.addFlashAttribute("success", "Enregistré !");
        } catch (Exception e) {
            e.printStackTrace();                      
            model.addAttribute("error", "Erreur technique : " + e.getMessage());
            chargerListes(model);
            return "demande/form";
        }
        return "redirect:/demandes";
    }

    private void chargerListes(Model model) {
        model.addAttribute("clients", clientRepo.findAll());
        model.addAttribute("communes", communeRepo.findAll());
    }

}
