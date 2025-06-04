package fortcultural.arquitetura.controller;

import fortcultural.arquitetura.dto.CulturalActivity;
import fortcultural.arquitetura.service.impl.CulturalActivityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activities")
public class CulturalActivityController {

    @Autowired
    private CulturalActivityServiceImpl activityService;

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

    @GetMapping("/filter")
    public ResponseEntity<List<CulturalActivity>> getFilteredActivities(@RequestParam String categoryOrTag) {
        return ResponseEntity.ok(activityService.getActivitiesFiltered(categoryOrTag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteCulturalActivity(id);
        return ResponseEntity.noContent().build();
    }
}
