package fortcultural.arquitetura.service.impl;

import fortcultural.arquitetura.dto.OSMActivityDTO;
import fortcultural.arquitetura.service.interfaces.OSMIntegrationService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class OSMIntegrationServiceImpl implements OSMIntegrationService {
    @Autowired
    private RestTemplate restTemplate;

    private static final String bbox = "-3.9383,-38.6750,-3.7184,-38.4220";

    private static final Logger logger = LoggerFactory.getLogger(OSMIntegrationServiceImpl.class);

    @Override
    public List<OSMActivityDTO> fetchCulturalActivitiesFromOSM(String tag, String bbox) {
        String overpassApiUrl = "https://overpass-api.de/api/interpreter";

        String query = String.format("""
        [out:json];
        node["amenity"="%s"](%s);
        out;
        """, tag, bbox);

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        URI uri = URI.create(overpassApiUrl + "?data=" + encodedQuery);

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        List<OSMActivityDTO> result = new ArrayList<>();

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                JSONObject json = new JSONObject(response.getBody());
                JSONArray elements = json.getJSONArray("elements");

                for (int i = 0; i < elements.length(); i++) {
                    JSONObject obj = elements.getJSONObject(i);
                    JSONObject tags = obj.optJSONObject("tags");

                    if (obj.has("lat") && obj.has("lon")) {
                        OSMActivityDTO activity = new OSMActivityDTO();
                        activity.setLatitude(obj.getDouble("lat"));
                        activity.setLongitude(obj.getDouble("lon"));
                        activity.setName(tags != null ? tags.optString("name", "Sem nome") : "Sem nome");
                        activity.setCategory(tags != null ? tags.optString("amenity", tag) : tag);
                        activity.setDescription(tags != null ? tags.optString("description", "") : "");

                        result.add(activity);
                    }
                }
            } catch (Exception e) {
                logger.error("Erro ao buscar dados no OSM", e);
            }
        }
        return result;
    }
}
