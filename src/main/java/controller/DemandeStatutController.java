package controller;

import dto.*;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import repository.*;
import service.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/statut")
@RequiredArgsConstructor
public class DemandeStatutController {
    private final DemandeRepository demandeRepo;
    private final DemandeStatutService demandeStatutService;
    private final StatutRepository statutRepo;
    private final DemandeStatutRepository demandeStatutRepo;

    @GetMapping
    public String list(Model model) {
        List<DemandeStatut> historiqueStatuts = demandeStatutRepo.findAllWithDemandeAndStatutOrderByDateStatutAsc();
        model.addAttribute("historiqueStatuts", historiqueStatuts);
        return "statut/list";
    }

    @GetMapping("/nouveau")
    public String showForm(Model model) {
        model.addAttribute("demandeStatutForm", new DemandeStatutFormDTO());
        chargerListes(model);
        return "statut/ajout";
    }

    @PostMapping
    public void save(@Valid @ModelAttribute DemandeStatutFormDTO dto,
            BindingResult result, Model model, HttpServletResponse response) throws IOException {
        if (result.hasErrors()) {
            System.out.println("Erreurs de validation : " + result.getAllErrors());
            chargerListes(model);
            response.sendRedirect("/statut/nouveau");
            return;
        }

        try {
            demandeStatutService.saveDemandeSTatut(dto);
            response.sendRedirect("/statut");
        } catch (Exception e) {
            System.err.println("Erreur technique : " + e.getMessage());
            model.addAttribute("error", "Erreur technique : " + e.getMessage());
            chargerListes(model);
            response.sendRedirect("/statut/nouveau");
        }
    }

    private void chargerListes(Model model) {
        model.addAttribute("demandes", demandeRepo.findAll());
        model.addAttribute("statuts", statutRepo.findAll());
    }

    @GetMapping("/modif/{idDemande}")
    public String showModifForm(@PathVariable Long idDemande, Model model) {
        DemandeStatut demandeStatut = demandeStatutRepo.findTopByDemandeIdOrderByDateStatutDescIdDesc(idDemande)
                .orElse(null);
        ModifStatutDTO dto = new ModifStatutDTO();
        dto.setIdDemande(idDemande);
        if (demandeStatut != null) {
            dto.setIdStatut(demandeStatut.getStatut() != null ? demandeStatut.getStatut().getId() : null);
            dto.setObservation(demandeStatut.getObservation());
            dto.setDate(demandeStatut.getDateStatut());
        }
        model.addAttribute("modifStatutForm", dto);
        model.addAttribute("demandes", demandeRepo.findAll());
        model.addAttribute("statuts", getStatutsExistants(idDemande));
        return "statut/modif";
    }

        @GetMapping("/modif")
        public String showModifBlank(Model model) {
            model.addAttribute("modifStatutForm", new ModifStatutDTO());
            model.addAttribute("demandes", demandeRepo.findAll());
            model.addAttribute("statuts", java.util.Collections.emptyList());
            return "statut/modif";
        }

        @GetMapping("/existants/{idDemande}")
        @ResponseBody
        public java.util.List<java.util.Map<String, Object>> getStatutsExistants(@PathVariable Long idDemande) {
            java.util.LinkedHashMap<Long, java.util.Map<String, Object>> uniques = new java.util.LinkedHashMap<>();
            var historiques = demandeStatutRepo.findByDemandeIdOrderByDateStatutAscIdAsc(idDemande);
            for (DemandeStatut ds : historiques) {
                if (ds.getStatut() == null) continue;
                java.util.Map<String, Object> item = new java.util.HashMap<>();
                item.put("idStatut", ds.getStatut().getId());
                item.put("libelle", ds.getStatut().getLibelle());
                uniques.put(ds.getStatut().getId(), item);
            }
            return new java.util.ArrayList<>(uniques.values());
        }

        @GetMapping("/current/{idDemande}")
        @ResponseBody
        public java.util.Map<String, Object> getCurrentForDemande(@PathVariable Long idDemande) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            var opt = demandeStatutRepo.findTopByDemandeIdOrderByDateStatutDescIdDesc(idDemande);
            if (opt.isPresent()) {
                DemandeStatut ds = opt.get();
                String dateStr = ds.getDateStatut() != null ? ds.getDateStatut().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";
                map.put("date", dateStr);
                map.put("idStatut", ds.getStatut() != null ? ds.getStatut().getId() : null);
                map.put("observation", ds.getObservation() != null ? ds.getObservation() : "");
            } else {
                map.put("date", "");
                map.put("idStatut", null);
                map.put("observation", "");
            }
            return map;
        }

        @GetMapping("/current/{idDemande}/{idStatut}")
        @ResponseBody
        public java.util.Map<String, Object> getCurrentForDemandeAndStatut(@PathVariable Long idDemande, @PathVariable Long idStatut) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            var opt = demandeStatutRepo.findTopByDemandeIdAndStatutIdOrderByDateStatutDescIdDesc(idDemande, idStatut);
            if (opt.isPresent()) {
                DemandeStatut ds = opt.get();
                String dateStr = ds.getDateStatut() != null ? ds.getDateStatut().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";
                map.put("date", dateStr);
                map.put("idStatut", ds.getStatut() != null ? ds.getStatut().getId() : null);
                map.put("observation", ds.getObservation() != null ? ds.getObservation() : "");
            } else {
                map.put("date", "");
                map.put("idStatut", null);
                map.put("observation", "");
            }
            return map;
        }

    @PostMapping("/modif")
    public void updateStatut(ModifStatutDTO dto, Model model, HttpServletResponse response) throws IOException {
        try {
            demandeStatutService.updateDemandeStatut(dto);
            response.sendRedirect("/statut");
        } catch (Exception e) {
            System.err.println("Erreur technique : " + e.getMessage());
            model.addAttribute("error", "Erreur technique : " + e.getMessage());
            chargerListes(model);
            response.sendRedirect("/statut/modif/" + dto.getIdDemande());
        }
    }
}