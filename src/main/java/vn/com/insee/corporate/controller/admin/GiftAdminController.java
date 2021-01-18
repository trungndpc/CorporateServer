package vn.com.insee.corporate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.insee.corporate.common.TypeGift;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.GiftForm;
import vn.com.insee.corporate.dto.response.GiftDTO;
import vn.com.insee.corporate.dto.response.admin.HistoryConstructionDTO;
import vn.com.insee.corporate.dto.response.admin.HistoryGiftDTO;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.GiftService;

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
            form.setType(TypeGift.CARD_PHONE.getType());
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

    @GetMapping(path = "/history", produces = {"application/json"})
    public ResponseEntity<BaseResponse> history() {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            List<HistoryGiftDTO> history = giftService.getHistory();
            response.setData(history);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }


}
