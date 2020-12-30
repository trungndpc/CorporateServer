package vn.com.insee.corporate.service.external;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Service
public class ZaloService {
    private final RestTemplate restTemplate;
    private static final long APP_ID = 191292518983577786l;
    private static final String SECRET_APP = "yspZ48H6T8LKh68ReMIz";
    private static final String GET_ACCESS_TOKEN_URL = "https://oauth.zaloapp.com/v3/access_token?app_id={1}&app_secret={2}&code={3}";
    private static final String GET_USER_INFO = "https://graph.zalo.me/v2.0/me?fields=id,name,picture,birthday,gender&access_token=";
    public static final String REDIRECT_AUTHEN_ZALO = "https://oauth.zaloapp.com/v3/auth?app_id=" + APP_ID + "&state=insee";
    private static final String ACCESS_TOKEN_OA = "EvbUCXk4omi_cbyv5eR61J2UKZWFtAm5Kw1vEXNXfdHbcKLB0jcm1IgaUaXwvEO60lylRtocm1GAnGjKIellSXRFAKagZSqbTkyBNJUa_p5SmXTOFxNDD7weDt01wlWkLB4y7Itoz7vFxtKH3wgULrhCVGXvbO5dM_j70dwCc5nVobvZV9s_7oBcG615lhiqPiP43aQAsMbcnZek18FLT5V8EZ8jWSnl2Dan75g2vL4yd2KF6yFnNaY16JWRpTvXJeuX3YRNu4XZZG0r8flxIclz6Z92Wjn0Gn4lh0iPcCC0";


    public ZaloService() {
        this.restTemplate = new RestTemplate();
    }

    public String getAccessToken(String oauthCode) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(GET_ACCESS_TOKEN_URL, String.class, APP_ID, SECRET_APP, oauthCode);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject json = new JSONObject(responseEntity.getBody());
            return  json.getString("access_token");
        }
        return null;
    }

    public ZaloUserEntity getUserInfo(String accessToken) {
        String url = GET_USER_INFO + accessToken;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject json = new JSONObject(responseEntity.getBody());
            ZaloUserEntity entity = new ZaloUserEntity();
            entity.setId(json.getString("id"));
            entity.setName(json.getString("name"));
            entity.setGender(json.getString("gender"));
            entity.setBirthday(json.getString("birthday"));
            entity.setAvatar(json.getJSONObject("picture").getJSONObject("data").getString("url"));
            return entity;
        }
        return null;
    }

}
