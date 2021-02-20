package vn.com.insee.corporate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.postgresql.util.GT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.common.status.CustomerStatus;
import vn.com.insee.corporate.common.status.StatusRegister;
import vn.com.insee.corporate.common.status.UserStatus;
import vn.com.insee.corporate.dto.RegisterForm;
import vn.com.insee.corporate.dto.response.UserDTO;
import vn.com.insee.corporate.dto.response.client.UserClientDTO;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.LoginFailedException;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.CustomerRepository;
import vn.com.insee.corporate.repository.UserRepository;
import vn.com.insee.corporate.service.external.ZaloService;
import vn.com.insee.corporate.service.external.ZaloUserEntity;
import vn.com.insee.corporate.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public StatusRegister getStatusRegister(int userId) {
        UserEntity userEntity = userRepository.getOne(userId);
        if (userEntity == null) {
            return StatusRegister.NOT_REGISTER;
        }
        String phone = userEntity.getPhone();
        if (phone == null) {
            return StatusRegister.NOT_REGISTER;
        }
        Integer customerId = userEntity.getCustomerId();
        if (customerId == null) {
            return StatusRegister.NOT_CUSTOMER;
        }
        CustomerEntity customerEntity = customerRepository.getOne(customerId);
        Integer customerStatus = customerEntity.getStatus();
        if (customerStatus == CustomerStatus.REVIEWING.getStatus()) {
            return StatusRegister.WAITING_REVIEW;
        }
        if (customerStatus == CustomerStatus.APPROVED.getStatus()) {
            return StatusRegister.REGISTERED;
        }
        if (customerStatus == CustomerStatus.REJECTED.getStatus()) {
            return StatusRegister.NEED_SUBMIT_AGAIN;
        }
        return StatusRegister.REGISTERED;
    }

    public UserClientDTO getByClient(Integer userId) {
        UserEntity userEntity = userRepository.getOne(userId);
        UserClientDTO userClientDTO = mapper.map(userEntity, UserClientDTO.class);
        if (userEntity.getFollowerZaloId() != null) {
            userClientDTO.setFollower(true);
        }else {
            userClientDTO.setFollower(false);
        }

        Integer customerId = userEntity.getCustomerId();
        String phone = userEntity.getPhone();
        CustomerEntity customerEntity = null;
        if (customerId != null) {
            customerEntity = customerRepository.getOne(customerId);
        }else if (phone != null) {
            customerEntity = customerRepository.findByPhone(phone);
        }
        if (customerEntity != null) {
            UserClientDTO.CustomerUserClientDTO customerUserClientDTO = mapper.map(customerEntity, UserClientDTO.CustomerUserClientDTO.class);
            userClientDTO.setCustomer(customerUserClientDTO);
        }
        return userClientDTO;
    }

    public UserDTO loginWithPassword(String phone, String pass, Permission permission) throws LoginFailedException {
        UserEntity userEntity = userRepository.findByPhone(phone);
        if (userEntity == null) {
            throw new LoginFailedException();
        }
        if (permission != null && permission.getId() != userEntity.getRoleId()) {
            throw new LoginFailedException();
        }
        String password = userEntity.getPassword();
        if (password == null || password.isEmpty() || pass == null || pass.isEmpty()) {
            throw new LoginFailedException();
        }
        if (!pass.equals(password)) {
            throw new LoginFailedException();
        }
        UserDTO userDTO = new UserDTO();
        mapper.map(userEntity, userDTO);
        return userDTO;
    }

    public UserDTO get(Integer id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isPresent()) {
            UserDTO userDTO = new UserDTO();
            mapper.map(optionalUserEntity.get(), userDTO);
            return userDTO;
        }
        return null;
    }

    public UserEntity initUserFromZalo(ZaloUserEntity zaloUserEntity) {
        System.out.println("initUserFromZalo");
        UserEntity userEntity = userRepository.findByZaloId(zaloUserEntity.getId());
        if (userEntity == null || userEntity.getRoleId() == null) {
            System.out.println("............");
            if(userEntity == null) {
                userEntity = new UserEntity();
                userEntity.setId(0);
            }
            userEntity.setRoleId(Permission.ANONYMOUS.getId());
            userEntity.setName(zaloUserEntity.getName());
            userEntity.setZaloId(zaloUserEntity.getId());
            userEntity.setPassword("");
            userEntity.setAvatar(zaloUserEntity.getAvatar());
            userEntity.setStatus(UserStatus.INIT_FROM_ZALO.getId());
            userEntity.setEnable(true);
            if (zaloUserEntity.getBirthday() != null) {
                int time = TimeUtil.getTime(zaloUserEntity.getBirthday());
                userEntity.setBirthday(time);
            }
            userEntity = userRepository.saveAndFlush(userEntity);
        }
        return userEntity;
    }


    public UserDTO create(RegisterForm registerForm, Permission role) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(registerForm.getFullName());
        userEntity.setPhone(registerForm.getPhone());
        userEntity.setStatus(UserStatus.INIT_FROM_WEB.getId());
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
            userEntity.setRoleId(Permission.CUSTOMER.getId());
            userRepository.saveAndFlush(userEntity);
        }
    }

    public void linkFollowerZaloWithUser(String zaloAppId, String followerId) {
        UserEntity userEntity = userRepository.findByZaloId(zaloAppId);
        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setZaloId(zaloAppId);
        }
        userEntity.setFollowerZaloId(followerId);
        userRepository.saveAndFlush(userEntity);
    }

    public void updatePassword(Integer userId, String password) {
        UserEntity userEntity = userRepository.getOne(userId);
        if (userEntity != null) {
            userEntity.setPassword(password);
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
