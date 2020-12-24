package vn.com.insee.corporate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.PermissionEnum;
import vn.com.insee.corporate.common.StatusEnum;
import vn.com.insee.corporate.common.UserStatusEnum;
import vn.com.insee.corporate.dto.RegisterForm;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.dto.response.UserDTO;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.UserRepository;
import vn.com.insee.corporate.service.external.ZaloUserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private UserRepository userRepository;

    public UserDTO findById(Integer id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isPresent()) {
            UserDTO userDTO = new UserDTO();
            mapper.map(optionalUserEntity.get(), userDTO);
            return userDTO;
        }
        return null;
    }

    public UserEntity initUserFromZalo(ZaloUserEntity zaloUserEntity) {
        UserEntity userEntity = userRepository.findByZaloId(zaloUserEntity.getId());
        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setId(0);
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

    public UserEntity update(int id, CustomerDTO customerDTO) throws NotExitException {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            throw new NotExitException();
        }
        UserEntity userEntity = optionalUserEntity.get();
        userEntity.setPhone(customerDTO.getPhone());
        userEntity.setName(customerDTO.getFullName());
        userEntity.setCustomerId(customerDTO.getId());
        userEntity = userRepository.saveAndFlush(userEntity);
        return userEntity;
    }

    public UserDTO create(RegisterForm registerForm, PermissionEnum role) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(registerForm.getFullName());
        userEntity.setPhone(registerForm.getPhone());
        userEntity.setStatus(UserStatusEnum.INIT_FROM_WEB.getId());
        userEntity.setRoleId(role.getId());
        userEntity.setEnable(true);
        userEntity = userRepository.saveAndFlush(userEntity);
        UserDTO userDTO = new UserDTO();
        mapper.map(userEntity, userDTO);
        return userDTO;
    }

    public void linkCustomerIdToUser(Integer userId, Integer customerId) {
        UserEntity userEntity = userRepository.getOne(userId);
        if (userEntity != null) {
            userEntity.setCustomerId(customerId);
            userEntity.setRoleId(PermissionEnum.CUSTOMER.getId());
            userRepository.saveAndFlush(userEntity);
        }
    }


    public boolean updateSession(int id, String session) throws NotExitException {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            throw new NotExitException();
        }
        UserEntity userEntity = optionalUserEntity.get();
        List<String> lstSession = userEntity.getLstSession();
        if (lstSession == null) {
            lstSession = new ArrayList<>();
        }
        if(lstSession.size() >=5){
            lstSession.remove(0);
        }
        lstSession.add(lstSession.size(), session);
        userEntity.setLstSession(lstSession);
        userRepository.saveAndFlush(userEntity);
        return true;
    }

    public UserDTO updatePhone(int id, String phone) throws NotExitException {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            throw new NotExitException();
        }
        UserEntity userEntity = optionalUserEntity.get();
        userEntity.setPhone(phone);
        userEntity = userRepository.saveAndFlush(userEntity);
        UserDTO userDTO = new UserDTO();
        mapper.map(userEntity, userDTO);
        return userDTO;
    }

    public UserDTO findByPhone(String phone) {
        UserEntity userEntity = userRepository.findByPhone(phone);
        if (userEntity != null) {
            UserDTO userDTO = new UserDTO();
            mapper.map(userEntity, userDTO);
            return userDTO;
        }
        return null;
    }
}
