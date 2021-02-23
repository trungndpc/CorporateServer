package vn.com.insee.corporate.common.dto;

public enum PromotionUserDTOStatus {
    WAITING_APPROVAL(1), WAITING_RECEIVED_GIFT(2), CAN_CREATE_NEW(3);

    private int status;

    PromotionUserDTOStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
