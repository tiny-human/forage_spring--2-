package repository;

import model.TypeDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeDevisRepository extends JpaRepository<TypeDevis, Long> {}
