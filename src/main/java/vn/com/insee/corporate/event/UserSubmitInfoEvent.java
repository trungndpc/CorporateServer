package vn.com.insee.corporate.event;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.com.insee.corporate.common.City;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.common.status.CustomerStatus;
import vn.com.insee.corporate.common.status.UserStatus;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.event.model.QuestionJoinPromotionModel;
import vn.com.insee.corporate.exception.FollowerExitException;
import vn.com.insee.corporate.repository.CustomerRepository;
import vn.com.insee.corporate.repository.UserRepository;
import vn.com.insee.corporate.service.external.zalo.webhook.UserShareInfoEvent;
import vn.com.insee.corporate.service.external.zalo.webhook.ZEvent;

@Component
public class UserSubmitInfoEvent {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private QuestionJoinPromotionModel questionJoinPromotionModel;

    public boolean is(UserShareInfoEvent event) {
        return "user_submit_info".equals(event.eventName);
    }

    public void process(UserShareInfoEvent event) throws FollowerExitException {
        UserShareInfoEvent.Info info = event.info;
        ZEvent.Sender sender = event.sender;
        UserEntity userEntity = userRepository.findByFollowerZaloId(sender.id);
        if (userEntity != null && userEntity.isValid()) {
            throw new FollowerExitException();
        }
        userEntity.setRoleId(Permission.CUSTOMER.getId());
        userEntity.setName(info.name);
        userEntity.setPhone(info.phone);
        userEntity.setFollowerZaloId(sender.id);
        userEntity.setStatus(UserStatus.INIT_FROM_ZALO.getId());
        userEntity = userRepository.saveAndFlush(userEntity);

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFullName(info.name);
        customerEntity.setPhone(info.phone);
        customerEntity.setMainAreaId(City.findByString(info.city));
        customerEntity.setLinkedUser(true);
        customerEntity.setUserId(userEntity.getId());
        customerEntity.setStatus(CustomerStatus.REVIEWING.getStatus());

        customerEntity = customerRepository.saveAndFlush(customerEntity);
        userEntity.setCustomerId(customerEntity.getId());
        userEntity.setEnable(true);
        userEntity.setStatus(UserStatus.INIT_FROM_ZALO.getId());
        userEntity = userRepository.saveAndFlush(userEntity);

    }

}
