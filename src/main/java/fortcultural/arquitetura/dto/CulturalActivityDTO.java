package fortcultural.arquitetura.dto;

public class CulturalActivityDTO {
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private String category;
    private boolean isFromOSM;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isFromOSM() {
        return isFromOSM;
    }

    public void setFromOSM(boolean fromOSM) {
        isFromOSM = fromOSM;
    }

    public static CulturalActivityDTO fromDataBase(fortcultural.arquitetura.model.entity.CulturalActivity activity) {
        CulturalActivityDTO dto = new CulturalActivityDTO();
        dto.name = activity.getName();
        dto.description = activity.getDescription();
        dto.latitude = activity.getLatitude();
        dto.longitude = activity.getLongitude();
        dto.category = activity.getCategory();
        dto.isFromOSM = false;
        return dto;
    }

    public static CulturalActivityDTO fromOSM(OSMActivityDTO activity) {
        CulturalActivityDTO dto = new CulturalActivityDTO();
        dto.name = activity.getName();
        dto.description = activity.getDescription();
        dto.latitude = activity.getLatitude();
        dto.longitude = activity.getLongitude();
        dto.category = activity.getCategory();
        dto.isFromOSM = true;
        return dto;
    }
}
