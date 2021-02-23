package vn.com.insee.corporate.controller.api.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.common.Permission;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.response.PromotionDTO;
import vn.com.insee.corporate.dto.response.client.PromotionCustomerDTO;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.CustomerExitException;
import vn.com.insee.corporate.exception.NeedToApprovalException;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.PromotionService;
import vn.com.insee.corporate.util.AuthenUtil;

import java.util.List;

@RestController
@RequestMapping("/api/promotion")
public class PromotionController {

    @Autowired
    private PromotionService postService;

    @GetMapping(path = "/list", produces = {"application/json"})
    public ResponseEntity<BaseResponse> list(Authentication authentication) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        try{
            Integer customerId = authUser != null ? authUser.getCustomerId() : null;
            Integer roleId = authUser != null ? authUser.getRoleId() : Permission.ANONYMOUS.getId();
            List<PromotionCustomerDTO> list = postService.getList(customerId, roleId);
            response.setData(list);
        }catch (NeedToApprovalException e) {
            response.setError(ErrorCode.NEED_TO_APPROVED);
        }catch (CustomerExitException e) {
            response.setError(ErrorCode.USER_NOT_IS_CUSTOMER);
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<BaseResponse> get(@RequestParam(required = true) int id, Authentication authentication) {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try {
            PromotionDTO promotionDTO = postService.get(id);
            response.setData(promotionDTO);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }
}
