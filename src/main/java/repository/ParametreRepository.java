package repository;

import java.util.List;

import model.Parametre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametreRepository extends JpaRepository<Parametre, Long> {

    List<Parametre> findByStatutSourceIdOrderByDebutAscFinAscIdAsc(Long idStatutSource);

    List<Parametre> findByStatutSourceIdAndStatutCibleIdOrderByDebutAscFinAscIdAsc(Long idStatutSource, Long idStatutCible);
}
