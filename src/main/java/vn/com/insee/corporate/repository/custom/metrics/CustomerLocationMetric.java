package vn.com.insee.corporate.repository.custom.entries;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerLocationEntry {
    private int total;
    @JsonProperty(value = "location_id")
    private int locationId;

    public CustomerLocationEntry(int total, int locationId) {
        this.total = total;
        this.locationId = locationId;
    }

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
