package vn.com.insee.corporate.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.repository.UserRepository;
import vn.com.insee.corporate.service.external.zalo.webhook.UserFollowOAEvent;
import vn.com.insee.corporate.service.external.zalo.webhook.UserSendMessageEvent;

@Service
public class UnfollowEvent {

    @Autowired
    private UserRepository userRepository;

    public void process(UserFollowOAEvent event) {
        String followerId = event.follower.id;
        UserEntity userEntity = userRepository.findByFollowerZaloId(followerId);
        if (userEntity != null) {
            userEntity.setFollowerZaloId(null);
        }
        userRepository.saveAndFlush(userEntity);
    }

    public boolean is(UserSendMessageEvent event) {
        return "unfollow".equals(event.eventName);
    }
}
