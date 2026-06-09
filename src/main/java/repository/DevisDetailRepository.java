package repository;

import model.DevisDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DevisDetailRepository extends JpaRepository<DevisDetail, Long> {
    List<DevisDetail> findByDevisId(Long idDevis);
}