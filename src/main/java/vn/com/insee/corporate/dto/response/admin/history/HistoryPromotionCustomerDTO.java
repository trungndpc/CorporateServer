package vn.com.insee.corporate.dto.response.admin.history;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.com.insee.corporate.dto.response.GiftDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoryPromotionCustomerDTO {
    private int id;
    private String title;
    private Long time;
    private int status;
    private GiftDTO gift;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public GiftDTO getGift() {
        return gift;
    }

    public void setGift(GiftDTO gift) {
        this.gift = gift;
    }
}
