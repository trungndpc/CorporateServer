package vn.com.insee.corporate.event;

import org.springframework.beans.factory.annotation.Autowired;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.repository.UserRepository;
import vn.com.insee.corporate.service.external.zalo.webhook.UserFollowOAEvent;

public class FollowEvent {

    @Autowired
    private UserRepository userRepository;

    public void process(UserFollowOAEvent event) {
        String followerId = event.follower.id;
        UserEntity userEntity = userRepository.findByFollowerZaloId(followerId);
        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setEnable(false);
        }
        userEntity.setFollowerZaloId(followerId);
        userRepository.saveAndFlush(userEntity);
    }
    public boolean is(UserFollowOAEvent event) {
        return "follow".equals(event.eventName);
    }
}
