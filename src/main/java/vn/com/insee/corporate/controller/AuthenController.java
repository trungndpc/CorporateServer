package vn.com.insee.corporate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.response.BaseResponse;

@RestController
@RequestMapping("/authen")
public class AuthenController {

    @GetMapping(path = "/zalo", produces = {"application/json"})
    public ResponseEntity<BaseResponse> get(@RequestParam(required = true) String code, @RequestParam(required = false) String redirectUrl) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            CustomerDTO customerDTO = service.get(id);
            if (customerDTO == null) {
                response.setError(ErrorCode.NOT_EXITS);
            }else{
                response.setData(customerDTO);
            }
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }
}
