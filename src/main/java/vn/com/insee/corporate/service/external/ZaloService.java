package vn.com.insee.corporate.service.external;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ZaloService {
//    private final RestTemplate restTemplate;
    private static final String GET_ACCESS_TOKEN_URL = "https://oauth.zaloapp.com/v3/access_token?app_id={1}&app_secret={2}&code={3}";

//    public ZaloService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    public ZaloTokenEntity getAccessToken(String oauthCode, long appId, String secret) {
//        ResponseEntity<ZaloTokenEntity> responseEntity = restTemplate.getForEntity(GET_ACCESS_TOKEN_URL, ZaloTokenEntity.class, appId, secret, oauthCode);
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            return responseEntity.getBody();
//        }
        return null;
    }
}
