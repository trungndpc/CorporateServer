package vn.com.insee.corporate.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends Exception{
    private static final String FORMAT = "httpStatus=%d, errorCode=%s, errorMsg=%s";
    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMsg;

    public BaseException() {}

    public BaseException(HttpStatus httpStatus, String errorCode, String errorMsg) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseException(HttpStatus httpStatus, String errorCode, String errorMsg, Throwable t) {
        super(t);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String getMessage() {
        return String.format(FORMAT, httpStatus.value(), errorCode, errorMsg);
    }
}
