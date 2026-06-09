package repository;

import model.Devis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Long> {
    List<Devis> findByDemandeId(Long idDemande);
    List<Devis> findAllByOrderByDateDevisAscIdAsc();

}
