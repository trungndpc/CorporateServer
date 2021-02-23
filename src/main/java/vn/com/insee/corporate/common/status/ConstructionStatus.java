package vn.com.insee.corporate.common.status;

public enum ConstructionStatus {
    WAITING_APPROVAL(1), APPROVED(2), REJECTED(3), SEND_GIFT(4), RECEIVED_GIFT(5), RE_SUBMIT(6);
    private int status;

    ConstructionStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static ConstructionStatus findByStatus(int status) {
        switch (status) {
            case 1 : return WAITING_APPROVAL;
            case 2 : return APPROVED;
            case 3 : return REJECTED;
            case 4 : return SEND_GIFT;
            case 5 : return RECEIVED_GIFT;
            case 6 : return RE_SUBMIT;
            default: return null;
        }
    }
}
