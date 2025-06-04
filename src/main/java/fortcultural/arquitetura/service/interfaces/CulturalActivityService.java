package fortcultural.arquitetura.service.interfaces;

import fortcultural.arquitetura.model.entity.CulturalActivity;
import fortcultural.arquitetura.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface CulturalActivityService {
    CulturalActivity createCulturalActivity(CulturalActivity culturalActivity);
    List<CulturalActivity> listCulturalActivity();
    Optional<CulturalActivity> findCulturalActivityById(Long id);
    CulturalActivity updateCulturalActivity(Long id, CulturalActivity updatedActivity);
    void deleteCulturalActivity(Long id);
}
