package vn.com.insee.corporate.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.common.TypeConstruction;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.ConstructionForm;
import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.NotSupportTypeConstruction;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.ConstructionService;
import vn.com.insee.corporate.util.AuthenUtil;

@RestController
@RequestMapping("/api/construction")
public class ConstructionController {

    @Autowired
    private ConstructionService constructionService;

    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> create(@RequestBody ConstructionForm form, Authentication authentication) {
        BaseResponse response = new BaseResponse();
        try{
            UserEntity authUser = AuthenUtil.getAuthUser(authentication);
            Integer type = form.getType();
            if (type == null || TypeConstruction.findByType(type) == null) {
                throw new NotSupportTypeConstruction();
            }

            ConstructionDTO constructionDTO = constructionService.create(form, authUser.getId());
            if (constructionDTO != null) {
                response.setError(ErrorCode.SUCCESS);
                response.setData(constructionDTO);
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
