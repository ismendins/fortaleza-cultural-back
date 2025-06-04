package fortcultural.arquitetura.service.impl;

import fortcultural.arquitetura.dto.OSMActivity;
import fortcultural.arquitetura.service.interfaces.OSMIntegrationService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class OSMIntegrationServiceImpl implements OSMIntegrationService {
    @Autowired
    private RestTemplate restTemplate;

    private static final String BBOX_FORTALEZA = "-3.9383,-38.6750,-3.7184,-38.4220";

    @Override
    public List<OSMActivity> fetchCulturalActivitiesFromOSM(String tag) {
        String overpassApiUrl = "https://overpass-api.de/api/interpreter";

        String query = String.format("""
        [out:json];
        node["amenity"="%s"](-3.9383,-38.6750,-3.7184,-38.4220);
        out;
        """, tag);

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        URI uri = URI.create(overpassApiUrl + "?data=" + encodedQuery);

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        List<OSMActivity> result = new ArrayList<>();

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                JSONObject json = new JSONObject(response.getBody());
                JSONArray elements = json.getJSONArray("elements");

                for (int i = 0; i < elements.length(); i++) {
                    JSONObject obj = elements.getJSONObject(i);
                    JSONObject tags = obj.optJSONObject("tags");

                    if (obj.has("lat") && obj.has("lon")) {
                        OSMActivity activity = new OSMActivity();
                        activity.setLatitude(obj.getDouble("lat"));
                        activity.setLongitude(obj.getDouble("lon"));
                        activity.setName(tags != null ? tags.optString("name", "Sem nome") : "Sem nome");
                        activity.setCategory(tags != null ? tags.optString("amenity", tag) : tag);
                        activity.setDescription(tags != null ? tags.optString("description", "") : "");

                        result.add(activity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
