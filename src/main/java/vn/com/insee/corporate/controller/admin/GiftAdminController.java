package vn.com.insee.corporate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.GiftForm;
import vn.com.insee.corporate.dto.PostForm;
import vn.com.insee.corporate.dto.response.GiftDTO;
import vn.com.insee.corporate.dto.response.HistoryDTO;
import vn.com.insee.corporate.dto.response.PromotionDTO;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.GiftService;
import vn.com.insee.corporate.service.HistoryService;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/gift")
public class GiftAdminController {

    @Autowired
    private GiftService giftService;

    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> create(@RequestBody GiftForm form) {
        BaseResponse response = new BaseResponse();
        try{
            System.out.println("POST");
            GiftDTO giftDTO = giftService.create(form);
            if (giftDTO != null) {
                response.setError(ErrorCode.SUCCESS);
                response.setData(giftDTO);
            }else{
                response.setError(ErrorCode.FAILED);
            }
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

}
