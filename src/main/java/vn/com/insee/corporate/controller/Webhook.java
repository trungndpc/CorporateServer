package vn.com.insee.corporate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.exception.InvalidSessionException;
import vn.com.insee.corporate.exception.ZaloWebhookException;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.external.zalo.webhook.*;


@RestController
public class Webhook {

    @Autowired
    private ObjectMapper objectMapper;


    @PostMapping(value = "/zalo-webhook", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> zaloWebhook(@RequestBody String body) throws InvalidSessionException {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{

        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }

    private ZEvent parse(JSONObject body) throws JsonProcessingException {
        String eventName = body.getString("event_name");
        ZEvent event = null;
        switch (eventName) {
            case "follow" : {
                event = objectMapper.readValue(body.toString(), UserFollowOAEvent.class);
                break;
            }
            case "unfollow" : {
                 event = objectMapper.readValue(body.toString(), UserUnFollowOAEvent.class);
                break;
            }
            case "user_submit_info" : {
                 event = objectMapper.readValue(body.toString(), UserShareInfoEvent.class);
                break;
            }
            default: {
                if (eventName.startsWith("user_send")) {
                    event = objectMapper.readValue(body.toString(), UserSendMessageEvent.class);
                }else {

                }
            }
        }
        return event;
    }
}
