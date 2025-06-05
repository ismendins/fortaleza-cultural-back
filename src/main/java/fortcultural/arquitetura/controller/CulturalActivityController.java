package fortcultural.arquitetura.controller;

import fortcultural.arquitetura.dto.CulturalActivityDTO;
import fortcultural.arquitetura.dto.OSMActivityDTO;
import fortcultural.arquitetura.service.impl.CulturalActivityServiceImpl;
import fortcultural.arquitetura.service.impl.OSMIntegrationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activities")
public class CulturalActivityController {

    @Autowired
    private CulturalActivityServiceImpl activityService;

    @Autowired
    private OSMIntegrationServiceImpl osmIntegrationService;

    @PostMapping
    public ResponseEntity<fortcultural.arquitetura.model.entity.CulturalActivity> createActivity(@RequestBody fortcultural.arquitetura.model.entity.CulturalActivity activity) {
        return ResponseEntity.ok(activityService.createCulturalActivity(activity));
    }

    @GetMapping
    public ResponseEntity<List<fortcultural.arquitetura.model.entity.CulturalActivity>> getAllActivities() {
        return ResponseEntity.ok(activityService.listCulturalActivity());
    }

    @GetMapping("/{id}")
    public ResponseEntity<fortcultural.arquitetura.model.entity.CulturalActivity> findCulturalActivityById(@PathVariable Long id) {
        Optional<fortcultural.arquitetura.model.entity.CulturalActivity> existingActivity = activityService.findCulturalActivityById(id);
        return existingActivity.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<fortcultural.arquitetura.model.entity.CulturalActivity> updateActivity(@PathVariable Long id, @RequestBody fortcultural.arquitetura.model.entity.CulturalActivity updatedActivity) {
        return ResponseEntity.ok(activityService.updateCulturalActivity(id, updatedActivity));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CulturalActivityDTO>> searchActivities(@RequestParam String categoryOrTag) {
        String bbox = "-3.9383,-38.6750,-3.7184,-38.4220";
        List<CulturalActivityDTO> localActivities = activityService.getActivitiesFiltered(categoryOrTag);
        List<OSMActivityDTO> osmActivities = osmIntegrationService.fetchCulturalActivitiesFromOSM(categoryOrTag, bbox);

        List<CulturalActivityDTO> allActivities = new ArrayList<>(localActivities);

        for (OSMActivityDTO osmActivity : osmActivities) {
            allActivities.add(CulturalActivityDTO.fromOSM(osmActivity));
        }

        return ResponseEntity.ok(allActivities);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteCulturalActivity(id);
        return ResponseEntity.noContent().build();
    }
}
