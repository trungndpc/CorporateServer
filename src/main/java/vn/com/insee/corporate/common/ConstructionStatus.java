package vn.com.insee.corporate.common;

public enum ConstructionStatus {
    WAITING_APPROVAL(1), APPROVED(2), REJECTED(3);
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
            default: return null;
        }
    }
}
