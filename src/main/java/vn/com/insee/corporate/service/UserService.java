package vn.com.insee.corporate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.PermissionEnum;
import vn.com.insee.corporate.common.StatusEnum;
import vn.com.insee.corporate.common.UserStatusEnum;
import vn.com.insee.corporate.dto.RegisterForm;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.repository.UserRepository;
import vn.com.insee.corporate.service.external.ZaloUserEntity;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity initUserFromZalo(ZaloUserEntity zaloUserEntity) {
        UserEntity userEntity = userRepository.findByZaloId(zaloUserEntity.getId());
        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setName(zaloUserEntity.getName());
            userEntity.setZaloId(zaloUserEntity.getId());
            userEntity.setPassword("");
            userEntity.setAvatar(zaloUserEntity.getAvatar());
            userEntity.setRoleId(PermissionEnum.ANONYMOUS.getId());
            userEntity.setStatus(UserStatusEnum.INIT_FROM_ZALO.getId());
            userEntity.setEnable(true);
            userEntity = userRepository.saveAndFlush(userEntity);
        }
        return userEntity;
    }

    public UserEntity update(int id, RegisterForm registerForm) throws NotExitException {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            throw new NotExitException();
        }
        UserEntity userEntity = optionalUserEntity.get();
        userEntity.setPhone(registerForm.getPhone());
        userEntity.setName(registerForm.getFullName());
        userEntity = userRepository.saveAndFlush(userEntity);
        return userEntity;
    }

    public UserEntity create(int id, RegisterForm registerForm) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(registerForm.getFullName());
        userEntity.setPhone(registerForm.getPhone());
        userEntity.setStatus(UserStatusEnum.INIT_FROM_WEB.getId());
        userEntity.setEnable(true);
        userEntity = userRepository.saveAndFlush(userEntity);
        return userEntity;
    }

    public boolean updateSession(int id, String session) throws NotExitException {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            throw new NotExitException();
        }
        UserEntity userEntity = optionalUserEntity.get();
        List<String> lstSession = userEntity.getLstSession();
        lstSession.add(session);
        userEntity.setLstSession(lstSession);
        userRepository.saveAndFlush(userEntity);
        return true;
    }
}
