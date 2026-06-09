package repository;

import model.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {

    // Récupère toutes les demandes avec client et commune en un seul JOIN
    // pour éviter le problème N+1 sur la liste
    @Query("SELECT d FROM Demande d JOIN FETCH d.client JOIN FETCH d.commune ORDER BY d.dateDemande ASC, d.id ASC")
    List<Demande> findAllWithDetails();

    @Query("SELECT d FROM Demande d JOIN FETCH d.client JOIN FETCH d.commune WHERE d.id = :id")
    Optional<Demande> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT d FROM Demande d JOIN FETCH d.client JOIN FETCH d.commune WHERE d.reference = :reference")
    Optional<Demande> findByReferenceWithDetails(@Param("reference") String reference);

    Optional<Demande> findByReference(String reference);

    // Pour générer la prochaine référence : compte + 1
    long countBy();
}