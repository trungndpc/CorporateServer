package vn.com.insee.corporate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.common.status.ConstructionStatus;
import vn.com.insee.corporate.common.status.CustomerStatus;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.common.status.StatusRegister;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.entity.*;
import vn.com.insee.corporate.repository.*;
import vn.com.insee.corporate.service.ConstructionService;
import vn.com.insee.corporate.service.CustomerService;
import vn.com.insee.corporate.service.GiftService;
import vn.com.insee.corporate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/ping")
public class PingController {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository repository;

    @Autowired
    private GiftService giftService;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConstructionRepository constructionRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private ConstructionService constructionService;

    @Autowired
    private GiftRepository giftRepository;


//    @GetMapping("/reset")
//    String reset(Authentication authentication) {
//        customerRepository.deleteAll();
//        repository.deleteAll();
//
//        UserEntity userEntity = new UserEntity();
//        userEntity.setName("Nguyễn Công Phượng");
//        userEntity.setAvatar("https://static2.yan.vn/YanNews/202006/202006030349344511-2fc01591-f2a8-45f8-a210-79f517c9297b.png");
//        userEntity.setPassword("1");
//        userEntity.setPhone("84972797200");
//        userEntity.setEnable(true);
//        userEntity.setStatus(1);
//        userEntity.setRoleId(Permission.ADMIN.getId());
//        repository.saveAndFlush(userEntity);
//        return "OK";
//    }

    @GetMapping("/dashboard")
    String dashboard(Authentication authentication) throws Exception {
        return "OK";
    }
    @GetMapping("/ping")
    String all(Authentication authentication) throws Exception {
        List<PromotionEntity> all = promotionRepository.findAll();
        System.out.println(all);
//        PageDTO<ConstructionDTO> list = constructionService.getList(2, null, 0, 100);
//        System.out.println(list);

//        StatusRegister statusRegister = userService.getStatusRegister(117);
//        System.out.println(statusRegister);

//        promotionRepository.deleteById(1000);
//        promotionRepository.deleteById(1001);
//        promotionRepository.deleteById(1002);
//        PromotionEntity one = promotionRepository.getOne(1004);
//        one.setLocation(7);
//        promotionRepository.saveAndFlush(one);

//        List<PromotionEntity> all = promotionRepository.findAll();
//        System.out.println(all);ory.getOne(1004);
//        one.setLocation(7);
//        promotionRepository.saveAndFlush(one);
//        List<HistoryGiftCustomerDTO> listByUid = giftService.getHistoryByCustomerId(25);
        return "OK";
    }

    @GetMapping("/delete-gift")
    String deleteGift(@RequestParam(required = false) int id) {
        GiftEntity giftEntity = giftRepository.getOne(id);

        ConstructionEntity constructionEntity = constructionRepository.getOne(giftEntity.getConstructionId());
        constructionEntity.setGiftId(null);
        constructionEntity.setStatus(ConstructionStatus.APPROVED.getStatus());
        constructionRepository.saveAndFlush(constructionEntity);

        giftRepository.deleteById(id);
        return "OK";
    }

    @GetMapping("/delete")
    String delete(@RequestParam(required = false) String phone, @RequestParam(required = false) int id) {
        UserEntity byPhone = repository.getOne(id);
        int uid = byPhone.getId();
        int customerID = byPhone.getCustomerId();
        List<ConstructionEntity> byCustomerId = constructionRepository.findByCustomerId(customerID);
        if (byCustomerId != null) {
            constructionRepository.deleteAll(byCustomerId);
        }
        List<HistoryEntity> byUserId = historyRepository.findByUserId(uid);
        if (byUserId != null) {
            historyRepository.deleteAll(byUserId);
        }
        repository.deleteById(id);

        customerRepository.deleteById(byPhone.getCustomerId());
        return "OK";
    }

//    @GetMapping("/clear-all")
//    String clear(Authentication authentication) {
//        customerRepository.deleteAll();
//        repository.deleteAll();
//        return "OK";
//    }
//
    @GetMapping("/add-admin")
    String addAdmin(Authentication authentication) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("INSEE Admin");
        userEntity.setAvatar("https://insee-promotion-vn.s3.us-east-2.amazonaws.com/static/images/log-admin.png");
        userEntity.setPassword("123");
        userEntity.setPhone("84972797200");
        userEntity.setEnable(true);
        userEntity.setStatus(1);
        userEntity.setRoleId(Permission.ADMIN.getId());
        repository.saveAndFlush(userEntity);
        return "OK";
    }
//
    @GetMapping("/auto-gen")
    String autoGen(Authentication authentication) {
        genCustomer("Contractor B", "https://f33-zpg.zdn.vn/4246271967914451302/c139cb61f461043f5d70.jpg"
        ,"84972699592", 6);
        genCustomer("Contractor C", "https://f36-zpg.zdn.vn/1730508780768327302/74cb19982698d6c68f89.jpg"
                , "84972659592", 7);
        genCustomer("Contractor D", "https://f22-zpg.zdn.vn/7089438183322163729/beac9448aa485a160359.jpg"
                ,"84972691592", 8);
        genCustomer("Contractor E", "https://f32-zpg.zdn.vn/5292562318383030202/21444b5a735a8304da4b.jpg"
                ,"84972669592", 1);

        return "OK";
    }
//
//
//
    protected void genCustomer(String name, String avatar, String phone, int mainArea) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFullName(name);
        customerEntity.setAvatar(avatar);
        customerEntity.setPhone(phone);
        customerEntity.setMainAreaId(mainArea);
        customerEntity.setStatus(CustomerStatus.APPROVED.getStatus());
        customerEntity.setBirthday(536950800);
        customerEntity.setLinkedUser(false);
        customerRepository.saveAndFlush(customerEntity);

    }
}
