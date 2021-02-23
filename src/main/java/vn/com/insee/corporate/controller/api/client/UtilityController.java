package vn.com.insee.corporate.controller.api.client;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.util.insee.INSEEMessage;
import vn.com.insee.corporate.util.insee.INSEEProductNotExitException;
import vn.com.insee.corporate.util.insee.INSEEUtil;

@RestController
@RequestMapping("/api/utility")
public class UtilityController {

    @GetMapping(path = "/co2")
    public ResponseEntity<BaseResponse> co2(@RequestParam(required = true) int productId, @RequestParam(required = true)  int bags) throws INSEEProductNotExitException {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            INSEEMessage inseeMessage = INSEEUtil.getMessageFromBuyingINSEECement(productId, bags);
            response.setData(inseeMessage);
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }
}
