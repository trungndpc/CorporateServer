package vn.com.insee.corporate.service.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.com.insee.corporate.common.Constant;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.repository.CustomerRepository;
import vn.com.insee.corporate.repository.UserRepository;
import vn.com.insee.corporate.service.external.zalo.ListMessage;
import vn.com.insee.corporate.service.external.zalo.Recipient;
import vn.com.insee.corporate.service.external.zalo.TextMessage;
import vn.com.insee.corporate.service.external.zalo.api.Message;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ZaloService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String GET_ACCESS_TOKEN_URL = "https://oauth.zaloapp.com/v3/access_token?app_id={1}&app_secret={2}&code={3}";
    private static final String GET_USER_INFO = "https://graph.zalo.me/v2.0/me?fields=id,name,picture,birthday,gender&access_token=";
    private static final String END_POINT = "https://openapi.zalo.me/v2.0/oa/message?access_token={1}";

    public ZaloService() {
        this.restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        this.objectMapper = new ObjectMapper();
    }

    public boolean sendMessage(String followerId, Message message) throws JsonProcessingException {
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        ResponseEntity<String> zaloResponseResponseEntity = restTemplate.postForEntity(END_POINT, json, String.class,
                Constant.ZALO_OA_ACCESS_TOKEN);
        return zaloResponseResponseEntity.getStatusCode() == HttpStatus.OK;
    }

    public void sendTxtMsg(int userId, String msg) throws JsonProcessingException {
        UserEntity userEntity = userRepository.getOne(userId);
        String followerZaloId = userEntity.getFollowerZaloId();
        if (followerZaloId == null || followerZaloId.isEmpty()) {
        }else{
            sendTextMsg(followerZaloId, msg);
        }
    }

    public void sendTxtMsgByCustomerId(int customerId, String msg) throws JsonProcessingException {
        CustomerEntity customerEntity = customerRepository.getOne(customerId);
        sendTxtMsg(customerEntity.getUserId(), msg);
    }

    public void sendActionList(int userId, String imgUrl, String url, String title, String subTitle) throws JsonProcessingException {
        UserEntity userEntity = userRepository.getOne(userId);
        String followerZaloId = userEntity.getFollowerZaloId();
        if (followerZaloId == null || followerZaloId.isEmpty()) {
        }else{
            sendActionList(followerZaloId, imgUrl, url, title, subTitle);
        }
    }

    public String getAccessToken(String oauthCode) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(GET_ACCESS_TOKEN_URL, String.class, Constant.ZALO_APP_ID, Constant.ZALO_SECRET_APP, oauthCode);
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
        ResponseEntity<String> zaloResponseResponseEntity = restTemplate.postForEntity(END_POINT, textJson, String.class, Constant.ZALO_OA_ACCESS_TOKEN);
        if (zaloResponseResponseEntity.getStatusCode() != HttpStatus.OK) {
            return false;
        }
        return true;
    }

    public boolean sendActionList(String followerId, String imgUrl, String url, String title, String subTitle) throws JsonProcessingException {
        ListMessage.Element element = new ListMessage.Element();
        element.setImageUrl(imgUrl);
        element.setTitle(title);
        element.setSubtitle(subTitle);
        ListMessage.Action action = new ListMessage.Action("oa.open.url", url);
        element.setDefaultAction(action);
        ListMessage listMessage = new ListMessage();
        listMessage.setRecipient(new Recipient(followerId));
        ListMessage.Message message = new ListMessage.Message();
        List<ListMessage.Element> elements = new ArrayList<>();
        elements.add(element);
        ListMessage.Payload payload = new ListMessage.Payload(elements);
        ListMessage.Attachment attachment = new ListMessage.Attachment(payload);
        message.setAttachment(attachment);
        listMessage.setMessage(message);

        String textJson = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(listMessage);
        ResponseEntity<String> zaloResponseResponseEntity = restTemplate.postForEntity(END_POINT, textJson, String.class, Constant.ZALO_OA_ACCESS_TOKEN);
        if (zaloResponseResponseEntity.getStatusCode() != HttpStatus.OK) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws JsonProcessingException {
//        Constant.ZALO_OA_ACCESS_TOKEN = "bmDgElzhiY-M1siy_sk-APe7OpxmTzjztaPI7V5w-d6K75yGc7RZLQngHJgRSyvb_dHiSvf5qs6MBaWeiI3cOBWiM0Q9GUTjkdjZ485SwKEGTaq9u63g6lT_JKtOJ-qxtMDACTXLv6NDSrClsq3QHj9q3XFaHDjy-qLwQjzGsYMtR09eibRP5urnLLEcMjivWa5yA9PAtLkuHNqVaKwXJQPo4pwuNBTTb7Ct6OToh0YyRYyUXcFoLO9MLqAyJOmEiLaEQAXbjIc3JZOyZ5JiRkHWCGU1JOrDNLxj9_bpj2a";
//        ZaloService zaloService = new ZaloService();
//        zaloService.sendTextMsg("6093726558823912784", "OK");
//        zaloService.sendActionList("8735999925442427033", "https://developers.zalo.me/web/static/zalo.png", "https://developers.zalo.me/web/static/zalo.png", "title", "sub");
    }
}
