package fortcultural.arquitetura.repository;

import fortcultural.arquitetura.model.entity.CulturalActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CulturalActivityRepository extends JpaRepository<CulturalActivity, Long> {
    @Query("SELECT c FROM CulturalActivity c WHERE LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%'))")
    List<CulturalActivity> findByCategoryContainingIgnoreCase(@Param("category") String category);
}
