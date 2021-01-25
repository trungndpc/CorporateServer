package vn.com.insee.corporate.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Constant {
    public static String ADMIN_DOMAIN;
    public static String CLIENT_DOMAIN;
    public static String PREFIX_ADMIN_CONTROLLER;
    public static String PREFIX_CLIENT_CONTROLLER;
    public static String ADMIN_DOMAIN_VERSION;
    public static String CLIENT_DOMAIN_VERSION;
    public static String CONTENT_RESPONSE_TO_REDIRECT = "REDIRECT";

    @Value("${domain.admin}")
    public void setAdminDomain(String adminDomain) {
        ADMIN_DOMAIN = adminDomain;
    }

    @Value("${domain.client}")
    public void setClientDomain(String clientDomain) {
        CLIENT_DOMAIN = clientDomain;
    }

    @Value("${domain.prefix.admin.controller}")
    public void setPrefixAdminController(String prefixAdminController) {
        PREFIX_ADMIN_CONTROLLER = prefixAdminController;
    }

    @Value("${domain.prefix.client.controller}")
    public void setPrefixClientController(String prefixClientController) {
        PREFIX_CLIENT_CONTROLLER = prefixClientController;
    }

    @Value("${domain.version.admin}")
    public void setAdminDomainVersion(String adminDomainVersion) {
        ADMIN_DOMAIN_VERSION = adminDomainVersion;
    }

    @Value("${domain.version.client}")
    public void setClientDomainVersion(String clientDomainVersion) {
        CLIENT_DOMAIN_VERSION = clientDomainVersion;
    }
}
