package vn.com.insee.corporate.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.response.GiftDTO;
import vn.com.insee.corporate.dto.response.HistoryDTO;
import vn.com.insee.corporate.dto.response.client.gift.HistoryGiftDTO;
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
    public ResponseEntity<BaseResponse> me(Authentication authentication) throws UnsupportedEncodingException {
        BaseResponse response = new BaseResponse();
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        try{
            if (authUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            List<HistoryGiftDTO> historyGiftDTOS = giftService.getListByUid(authUser.getId());
            response.setData(historyGiftDTOS);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }

        return ResponseEntity.ok(response);
    }
}
