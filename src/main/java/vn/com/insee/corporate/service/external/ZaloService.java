package vn.com.insee.corporate.service.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.com.insee.corporate.exception.ZaloException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.service.external.zalo.TextMessage;
import vn.com.insee.corporate.service.external.zalo.ZaloResponse;

import java.nio.charset.Charset;
import java.util.Collection;

@Service
public class ZaloService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final long APP_ID = 191292518983577786l;
    private static final String SECRET_APP = "yspZ48H6T8LKh68ReMIz";
    private static final String GET_ACCESS_TOKEN_URL = "https://oauth.zaloapp.com/v3/access_token?app_id={1}&app_secret={2}&code={3}";
    private static final String GET_USER_INFO = "https://graph.zalo.me/v2.0/me?fields=id,name,picture,birthday,gender&access_token=";
    public static final String REDIRECT_AUTHEN_ZALO = "https://oauth.zaloapp.com/v3/auth?app_id=" + APP_ID + "&state=insee";
    private static final String ACCESS_TOKEN_OA = "pGEc8ySgAbF90ASeoYWdNSO4gYdJV5edWbQWLSbxON-84ffRzL1VA88VamtiHr81lMoh3-uRJXgTEBuVdsbC3-8Ae2VWINWfwXMeBU1SVYsk39mPmcL03vmjZJJF0my2fq_B1CaBI0c088WQZ7LD5OKzjcVh2ovGdIRnTDTfALMRNwr-aZnv3TeJe3YHC6O9ucg39gaBO2NbKQ80jJakAVSIxXos90qfqaht4hCh4nNdLk0awZLd9-HUjrUo76T4naktOR4_U0xFHg4bdZjy3kb6gZdWCb4mCpL7EnkrGiKs8bW";
    private static final String SEND_MSG_TO_USER_URL = "https://openapi.zalo.me/v2.0/oa/message?access_token={1}";


    public ZaloService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
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

    public boolean sendTextMsg(String followerId, String text) throws JsonProcessingException {
        TextMessage textMessage = new TextMessage(followerId, text);
        String textJson = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(textMessage);
        ResponseEntity<String> zaloResponseResponseEntity = restTemplate.postForEntity(SEND_MSG_TO_USER_URL, textJson, String.class, ACCESS_TOKEN_OA);
        if (zaloResponseResponseEntity.getStatusCode() != HttpStatus.OK) {
            System.out.println(zaloResponseResponseEntity.getBody());
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws JsonProcessingException {
        ZaloService zaloService = new ZaloService();
        String c = "Chúc mừng";
        zaloService.sendTextMsg("8735999925442427033", c);
    }

}
