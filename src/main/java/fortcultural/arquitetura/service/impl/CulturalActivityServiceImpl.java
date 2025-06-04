package fortcultural.arquitetura.service.impl;

import fortcultural.arquitetura.model.entity.CulturalActivity;
import fortcultural.arquitetura.repository.CulturalActivityRepository;
import fortcultural.arquitetura.service.interfaces.CulturalActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CulturalActivityServiceImpl implements CulturalActivityService {

    @Autowired
    private CulturalActivityRepository culturalActivityRepository;

    @Override
    public CulturalActivity createCulturalActivity(CulturalActivity culturalActivity) {
        return culturalActivityRepository.save(culturalActivity);
    }

    @Override
    public Optional<CulturalActivity> findCulturalActivityById(Long id) {
        return culturalActivityRepository.findById(id);
    }

    @Override
    public List<CulturalActivity> listCulturalActivity() {
        return culturalActivityRepository.findAll();
    }

    @Override
    public CulturalActivity updateCulturalActivity(Long id, CulturalActivity updatedActivity) {
        return culturalActivityRepository.findById(id).map(culturalActivity -> {
            culturalActivity.setName(updatedActivity.getName());
            culturalActivity.setDescription(updatedActivity.getDescription());
            culturalActivity.setLatitude(updatedActivity.getLatitude());
            culturalActivity.setLongitude(updatedActivity.getLongitude());
            culturalActivity.setCategory(updatedActivity.getCategory());
            culturalActivity.setOrganizer(updatedActivity.getOrganizer());
            return culturalActivityRepository.save(culturalActivity);
    }).orElseThrow(() -> new RuntimeException("Atividade cultura com ID " +  id + " não encontrado."));
    }

    @Override
    public void deleteCulturalActivity(Long id) {
        culturalActivityRepository.deleteById(id);
    }
}
