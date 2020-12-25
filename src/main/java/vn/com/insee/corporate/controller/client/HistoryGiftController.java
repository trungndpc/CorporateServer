package vn.com.insee.corporate.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.HistoryForm;
import vn.com.insee.corporate.dto.response.HistoryDTO;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.HistoryService;
import vn.com.insee.corporate.util.AuthenUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryGiftController {

    @Autowired
    private HistoryService historyService;

    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> create(@RequestBody HistoryForm form) {
        BaseResponse response = new BaseResponse();
        try{
            HistoryDTO historyDTO = historyService.create(form);
            if (historyDTO != null) {
                response.setError(ErrorCode.SUCCESS);
                response.setData(historyDTO);
            }else{
                response.setError(ErrorCode.FAILED);
            }
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<BaseResponse> getUserInfo(Authentication authentication) throws UnsupportedEncodingException {
        BaseResponse response = new BaseResponse();
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        try{
            if (authUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            List<HistoryDTO> historyDTOS = historyService.findByUserId(authUser.getId());
            response.setData(historyDTOS);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }

        return ResponseEntity.ok(response);
    }
}
