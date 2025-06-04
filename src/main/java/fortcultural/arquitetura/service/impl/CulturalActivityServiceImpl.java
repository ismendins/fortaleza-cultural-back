package fortcultural.arquitetura.service.impl;

import fortcultural.arquitetura.dto.CulturalActivity;
import fortcultural.arquitetura.dto.OSMActivity;
import fortcultural.arquitetura.repository.CulturalActivityRepository;
import fortcultural.arquitetura.service.interfaces.CulturalActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CulturalActivityServiceImpl implements CulturalActivityService {

    @Autowired
    private OSMIntegrationServiceImpl osmIntegrationService;

    @Autowired
    private CulturalActivityRepository culturalActivityRepository;

    @Override
    public fortcultural.arquitetura.model.entity.CulturalActivity createCulturalActivity(fortcultural.arquitetura.model.entity.CulturalActivity culturalActivity) {
        return culturalActivityRepository.save(culturalActivity);
    }

    @Override
    public Optional<fortcultural.arquitetura.model.entity.CulturalActivity> findCulturalActivityById(Long id) {
        return culturalActivityRepository.findById(id);
    }

    public List<CulturalActivity> getAllActivities(String tag) {
        List<CulturalActivity> result = new ArrayList<>();

        List<fortcultural.arquitetura.model.entity.CulturalActivity> local = culturalActivityRepository.findAll();
        for (fortcultural.arquitetura.model.entity.CulturalActivity activity : local) {
            result.add(CulturalActivity.fromDataBase(activity));
        }

        List<OSMActivity> osm = osmIntegrationService.fetchCulturalActivitiesFromOSM(tag);
        for (OSMActivity activity : osm) {
            result.add(CulturalActivity.fromOSM(activity));
        }

        return result;
    }


    public List<CulturalActivity> getActivitiesFiltered(String categoryOrTag) {
        List<CulturalActivity> result = new ArrayList<>();

        List<fortcultural.arquitetura.model.entity.CulturalActivity> local = culturalActivityRepository.findByCategoryContainingIgnoreCase(categoryOrTag);
        for (fortcultural.arquitetura.model.entity.CulturalActivity activity : local) {
            result.add(CulturalActivity.fromDataBase(activity));
        }

        List<OSMActivity> osm = osmIntegrationService.fetchCulturalActivitiesFromOSM(categoryOrTag);
        for (OSMActivity activity : osm) {
            result.add(CulturalActivity.fromOSM(activity));
        }

        return result;
    }


    @Override
    public List<fortcultural.arquitetura.model.entity.CulturalActivity> listCulturalActivity() {
        return culturalActivityRepository.findAll();
    }

    @Override
    public fortcultural.arquitetura.model.entity.CulturalActivity updateCulturalActivity(Long id, fortcultural.arquitetura.model.entity.CulturalActivity updatedActivity) {
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
