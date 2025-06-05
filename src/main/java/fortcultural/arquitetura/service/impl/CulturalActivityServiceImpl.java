package fortcultural.arquitetura.service.impl;

import fortcultural.arquitetura.dto.CulturalActivityDTO;
import fortcultural.arquitetura.dto.OSMActivityDTO;
import fortcultural.arquitetura.model.entity.User;
import fortcultural.arquitetura.repository.CulturalActivityRepository;
import fortcultural.arquitetura.service.interfaces.CulturalActivityService;
import fortcultural.arquitetura.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private UserRepository userRepository;

    private static final String BBOX = "-3.9383,-38.6750,-3.7184,-38.4220";

    @Override
    public fortcultural.arquitetura.model.entity.CulturalActivity createCulturalActivity(fortcultural.arquitetura.model.entity.CulturalActivity culturalActivity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> existingUser = userRepository.findByEmail(username);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        User organizer = existingUser.get();
        culturalActivity.setOrganizer(organizer);

        return culturalActivityRepository.save(culturalActivity);
    }

    @Override
    public Optional<fortcultural.arquitetura.model.entity.CulturalActivity> findCulturalActivityById(Long id) {
        return culturalActivityRepository.findById(id);
    }

    public List<CulturalActivityDTO> getAllActivities(String tag) {
        List<CulturalActivityDTO> result = new ArrayList<>();

        List<fortcultural.arquitetura.model.entity.CulturalActivity> local = culturalActivityRepository.findAll();
        for (fortcultural.arquitetura.model.entity.CulturalActivity activity : local) {
            result.add(CulturalActivityDTO.fromDataBase(activity));
        }

        List<OSMActivityDTO> osm = osmIntegrationService.fetchCulturalActivitiesFromOSM(tag, BBOX);
        for (OSMActivityDTO activity : osm) {
            result.add(CulturalActivityDTO.fromOSM(activity));
        }

        return result;
    }

    public List<CulturalActivityDTO> getActivitiesFiltered(String categoryOrTag) {
        List<CulturalActivityDTO> result = new ArrayList<>();

        List<fortcultural.arquitetura.model.entity.CulturalActivity> local = culturalActivityRepository.findByCategoryContainingIgnoreCase(categoryOrTag);
        for (fortcultural.arquitetura.model.entity.CulturalActivity activity : local) {
            result.add(CulturalActivityDTO.fromDataBase(activity));
        }

        List<OSMActivityDTO> osm = osmIntegrationService.fetchCulturalActivitiesFromOSM(categoryOrTag, BBOX);
        for (OSMActivityDTO activity : osm) {
            result.add(CulturalActivityDTO.fromOSM(activity));
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
