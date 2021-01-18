package vn.com.insee.corporate.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.common.GiftStatus;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.response.GiftDTO;
import vn.com.insee.corporate.dto.response.client.HistoryGiftCustomerDTO;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.GiftService;
import vn.com.insee.corporate.util.AuthenUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/gift")
public class GiftController {

    @Autowired
    private GiftService giftService;

    @GetMapping(path = "/me")
    public ResponseEntity<BaseResponse> getUserInfo(Authentication authentication) throws UnsupportedEncodingException {
        BaseResponse response = new BaseResponse();
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        try{
            if (authUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            List<HistoryGiftCustomerDTO> historyDTOS = giftService.getHistoryByCustomerId(authUser.getCustomerId());
            response.setData(historyDTOS);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/get")
    public ResponseEntity<BaseResponse> getById(@RequestParam(required = true) int id, Authentication authentication) {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        BaseResponse response = new BaseResponse();
        try{
            if (authUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            GiftDTO giftDTO = giftService.get(id);
            if (giftDTO.getCustomerId() != authUser.getCustomerId()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            filter(giftDTO);
            response.setData(giftDTO);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/received")
    public ResponseEntity<BaseResponse> received(@RequestParam(required = true) int id, Authentication authentication) {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        BaseResponse response = new BaseResponse();
        try{
            if (authUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            giftService.received(id, authUser.getCustomerId());
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }



    private void filter(GiftDTO giftDTO) {
        if (giftDTO.getStatus() != GiftStatus.RECEIVED.getStatus()) {
            giftDTO.setCards(null);
        }
    }
}
