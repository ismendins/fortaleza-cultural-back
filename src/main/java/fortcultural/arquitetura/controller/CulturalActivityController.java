package fortcultural.arquitetura.controller;

import fortcultural.arquitetura.model.entity.CulturalActivity;
import fortcultural.arquitetura.service.impl.CulturalActivityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class CulturalActivityController {

    @Autowired
    private CulturalActivityServiceImpl activityService;

    @PostMapping
    public ResponseEntity<CulturalActivity> createActivity(@RequestBody CulturalActivity activity) {
        return ResponseEntity.ok(activityService.createCulturalActivity(activity));
    }

    @GetMapping
    public ResponseEntity<List<CulturalActivity>> getAllActivities() {
        return ResponseEntity.ok(activityService.listCulturalActivity());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CulturalActivity> findCulturalActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.findCulturalActivityById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CulturalActivity> updateActivity(@PathVariable Long id, @RequestBody CulturalActivity updatedActivity) {
        return ResponseEntity.ok(activityService.updateCulturalActivity(id, updatedActivity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteCulturalActivity(id);
        return ResponseEntity.noContent().build();
    }
}
