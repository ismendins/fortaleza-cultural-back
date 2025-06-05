package fortcultural.arquitetura.service.interfaces;

import fortcultural.arquitetura.dto.OSMActivityDTO;

import java.util.List;

public interface OSMIntegrationService {
    public List<OSMActivityDTO> fetchCulturalActivitiesFromOSM(String tag, String bbox);
}
