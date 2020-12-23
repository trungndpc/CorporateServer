package vn.com.insee.corporate.util;

import org.springframework.security.core.Authentication;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.security.InseeUserDetail;

public class AuthenUtil {

    public static UserEntity getAuthUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        InseeUserDetail userDetails = (InseeUserDetail) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
