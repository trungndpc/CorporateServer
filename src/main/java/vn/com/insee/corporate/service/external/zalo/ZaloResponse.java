package vn.com.insee.corporate.service.external.zalo;

public class ZaloResponse {
    private int error;
    private String message;
    private ZaloMessageDataResponse data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZaloMessageDataResponse getData() {
        return data;
    }

    public void setData(ZaloMessageDataResponse data) {
        this.data = data;
    }
}
