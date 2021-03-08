package vn.com.insee.corporate.dto.response.admin.report.metric;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerLocationMetricDTO {
    private int total;
    @JsonProperty(value = "location_id")
    private int locationId;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}
