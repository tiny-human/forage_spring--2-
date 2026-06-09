package repository;

import java.util.List;

import model.DemandeStatut;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DemandeStatutRepository extends JpaRepository<DemandeStatut, Long> {

    // Dernier statut d'une demande (pour affichage)
    // Utilise le parsing de nom de méthode Spring Data JPA pour limiter la requête
    Optional<DemandeStatut> findTopByDemandeIdOrderByDateStatutDescIdDesc(Long idDemande);

    Optional<DemandeStatut> findTopByDemandeIdAndStatutIdOrderByDateStatutDescIdDesc(Long idDemande, Long idStatut);

    List<DemandeStatut> findByDemandeIdOrderByDateStatutDescIdDesc(Long idDemande);

    List<DemandeStatut> findByDemandeIdOrderByDateStatutAscIdAsc(Long idDemande);

    @Query("select ds from DemandeStatut ds join fetch ds.demande join fetch ds.statut order by ds.dateStatut asc, ds.id asc")
    List<DemandeStatut> findAllWithDemandeAndStatutOrderByDateStatutAsc();

    DemandeStatut findByDemandeId(Long idDemande);


}