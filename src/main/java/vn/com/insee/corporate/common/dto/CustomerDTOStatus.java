package vn.com.insee.corporate.common.dto;

import vn.com.insee.corporate.common.CustomerStatus;

public enum  CustomerDTOStatus {
    DO_NOT_HAVE_ACCOUNT(1), NEED_REVIEW(2), REJECTED(3), APPROVED(4) ;

    CustomerDTOStatus(int status) {
        this.status = status;
    }

    private int status;

    public static CustomerDTOStatus findBy(CustomerStatus status, boolean isLinkedUser) {
        if (isLinkedUser) {
            if (status.equals(CustomerStatus.APPROVED)) {
                return APPROVED;
            }else if (status.equals(CustomerStatus.REJECTED)) {
                return REJECTED;
            }else if (status.equals(CustomerStatus.REVIEWING)) {
                return NEED_REVIEW;
            }
        }else{
            return DO_NOT_HAVE_ACCOUNT;
        }
        return null;
    }

    public static CustomerDTOStatus findByStatus(int status) {
        switch (status) {
            case 1: return DO_NOT_HAVE_ACCOUNT;
            case 2: return NEED_REVIEW;
            case 3: return REJECTED;
            case 4: return APPROVED;
            default: return null;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
