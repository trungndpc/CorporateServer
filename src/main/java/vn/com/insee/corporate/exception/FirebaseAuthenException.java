package vn.com.insee.corporate.exception;

import org.springframework.http.HttpStatus;

public class FirebaseAuthenException extends BaseException{
    public enum FirebaseAuthenError {
        AUTH_ERROR(HttpStatus.BAD_REQUEST, "Firebase_auth_error", "Lỗi khi xác thực");

        FirebaseAuthenError(HttpStatus httpStatus, String errorCode, String description) {
            this.httpStatus = httpStatus;
            this.errorCode = errorCode;
            this.description = description;
        }

        private final HttpStatus httpStatus;
        private final String errorCode;
        private final String description;

        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public String getDescription() {
            return description;
        }
    }

    public FirebaseAuthenException(FirebaseAuthenError firebaseAuthenError) {
        super(firebaseAuthenError.getHttpStatus(), firebaseAuthenError.getErrorCode(), firebaseAuthenError.getDescription());
    }

    public FirebaseAuthenException(FirebaseAuthenError firebaseAuthenError, Throwable cause) {
        super(firebaseAuthenError.getHttpStatus(), firebaseAuthenError.getErrorCode(), firebaseAuthenError.getDescription(), cause);
    }
}
