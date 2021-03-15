package vn.com.insee.corporate.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.event.model.QuestionJoinPromotionModel;
import vn.com.insee.corporate.event.model.RegisterModel;
import vn.com.insee.corporate.repository.UserRepository;
import vn.com.insee.corporate.service.external.ZaloService;
import vn.com.insee.corporate.service.external.zalo.api.Message;
import vn.com.insee.corporate.service.external.zalo.webhook.UserSendMessageEvent;
import vn.com.insee.corporate.service.external.zalo.webhook.ZEvent;

import java.util.Arrays;

@Component
public class RegisterEvent {

    @Autowired
    private RegisterModel registerModel;

    @Autowired
    private QuestionJoinPromotionModel questionJoinPromotionModel;

    @Autowired
    private UserRepository userRepository;

    public void process(UserSendMessageEvent event) throws JsonProcessingException {
        ZEvent.Sender sender = event.sender;
        UserEntity userEntity = getByFollower(sender.id);
        if (userEntity == null || !userEntity.isValid()) {
            registerModel.sendLinkToRegister(userEntity.getFollowerZaloId());
            return;
        }
        questionJoinPromotionModel.sendAfterSuccessRegister(userEntity.getFollowerZaloId());
    }


    public boolean is(UserSendMessageEvent event) {
        return "user_send_text".equals(event.eventName) && "#register".equals(event.message.text);
    }

    private UserEntity getByFollower(String followerId) {
        return userRepository.findByFollowerZaloId(followerId);
    }


}