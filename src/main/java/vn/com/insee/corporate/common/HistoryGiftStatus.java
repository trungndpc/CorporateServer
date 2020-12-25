package vn.com.insee.corporate.common;

public enum HistoryGiftStatus {
    SEND(1), RECEIVED(2);
    private int status;

    HistoryGiftStatus(int status) {
        this.status = status;
    }

    public static HistoryGiftStatus findByStatus(int status) {
        switch (status) {
            case 1 : return SEND;
            case 2 : return RECEIVED;
            default: return null;
        }
    }

    public int getStatus() {
        return status;
    }


}
