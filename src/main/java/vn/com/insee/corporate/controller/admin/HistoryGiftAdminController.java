package vn.com.insee.corporate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.response.HistoryDTO;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.HistoryService;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/promotion")
public class HistoryGiftAdminController {

    @Autowired
    private HistoryService historyService;

    @GetMapping(path = "/user")
    public ResponseEntity<BaseResponse> getUserInfo(@RequestParam(required = true) int userId, Authentication authentication) throws UnsupportedEncodingException {
        BaseResponse response = new BaseResponse();
        try{
            List<HistoryDTO> historyDTOS = historyService.findByUserId(userId);
            response.setData(historyDTOS);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }

        return ResponseEntity.ok(response);
    }
}
