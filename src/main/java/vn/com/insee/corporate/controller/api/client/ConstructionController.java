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
import vn.com.insee.corporate.util.insee.INSEEMessage;
import vn.com.insee.corporate.util.insee.INSEEUtil;

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
            ConstructionDTO constructionDTO;
            if (form.getId() == null) {
                Integer type = form.getType();
                if (type == null || TypeConstruction.findByType(type) == null) {
                    throw new NotSupportTypeConstruction();
                }
                constructionDTO = constructionService.create(form, authUser.getCustomerId());
            }else {
                constructionDTO = constructionService.update(form, authUser.getCustomerId());
            }
            response.setData(constructionDTO);
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
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/summary", produces = {"application/json"})
    public ResponseEntity<BaseResponse> getStatus(@RequestParam(required = true) int id, Authentication authentication)  {
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
            INSEEMessage inseeMessage = INSEEUtil.getMessageFromBuyingINSEECement(constructionDTO.getCement(), constructionDTO.getQuantity());
            response.setData(inseeMessage);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }


}
