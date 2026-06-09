package repository;

import java.util.List;

import model.Parametre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametreRepository extends JpaRepository<Parametre, Long> {

    List<Parametre> findByStatutSourceIdOrderByDureeAscIdAsc(Long idStatutSource);

    List<Parametre> findByStatutSourceIdAndStatutCibleIdOrderByDureeAscIdAsc(Long idStatutSource, Long idStatutCible);
}
