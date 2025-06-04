package fortcultural.arquitetura.dto;

public class CulturalActivity {
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private String category;
    private boolean isFromOSM;

    public static CulturalActivity fromDataBase(fortcultural.arquitetura.model.entity.CulturalActivity activity) {
        CulturalActivity dto = new CulturalActivity();
        dto.name = activity.getName();
        dto.description = activity.getDescription();
        dto.latitude = activity.getLatitude();
        dto.longitude = activity.getLongitude();
        dto.category = activity.getCategory();
        dto.isFromOSM = false;
        return dto;
    }

    public static CulturalActivity fromOSM(OSMActivity activity) {
        CulturalActivity dto = new CulturalActivity();
        dto.name = activity.getName();
        dto.description = activity.getDescription();
        dto.latitude = activity.getLatitude();
        dto.longitude = activity.getLongitude();
        dto.category = activity.getCategory();
        dto.isFromOSM = true;
        return dto;
    }
}
