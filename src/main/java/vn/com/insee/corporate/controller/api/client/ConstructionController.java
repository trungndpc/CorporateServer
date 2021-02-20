package vn.com.insee.corporate.controller.api.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.com.insee.corporate.common.type.TypeConstruction;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.ConstructionForm;
import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.InvalidSessionException;
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
            if (form.getId() == null) {
                Integer type = form.getType();
                if (type == null || TypeConstruction.findByType(type) == null) {
                    throw new NotSupportTypeConstruction();
                }
                constructionService.create(form, authUser.getCustomerId());
            }else {
                constructionService.update(form, authUser.getCustomerId());
            }
            response.setError(ErrorCode.SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping(produces = {"application/json"})
    public ResponseEntity<BaseResponse> getById(@RequestParam(required = true) int id, Authentication authentication)  {
        UserEntity authUser = AuthenUtil.getAuthUser(authentication);
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            ConstructionDTO constructionDTO = constructionService.get(id);
            if (constructionDTO.getUser().getId() != authUser.getId()) {
                throw new InvalidSessionException();
            }
            response.setData(constructionDTO);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }
}