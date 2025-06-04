package fortcultural.arquitetura.service.interfaces;

import fortcultural.arquitetura.dto.OSMActivity;

import java.util.List;

public interface OSMIntegrationService {
    public List<OSMActivity> fetchCulturalActivitiesFromOSM(String tag);
}
