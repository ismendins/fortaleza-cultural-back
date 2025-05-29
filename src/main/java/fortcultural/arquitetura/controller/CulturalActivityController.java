package fortcultural.arquitetura.controller;

import fortcultural.arquitetura.model.entity.CulturalActivity;
import fortcultural.arquitetura.repository.CulturalActivityRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
public class CulturalActivityController {
    private CulturalActivityRepository culturalActivityService;


}
