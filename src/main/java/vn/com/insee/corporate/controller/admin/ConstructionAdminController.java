package vn.com.insee.corporate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.com.insee.corporate.common.ConstructionStatus;
import vn.com.insee.corporate.common.CustomerStatus;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.dto.response.UserDTO;
import vn.com.insee.corporate.entity.UserEntity;
import vn.com.insee.corporate.exception.InvalidSessionException;
import vn.com.insee.corporate.exception.StatusNotSupportException;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.ConstructionService;
import vn.com.insee.corporate.service.UserService;
import vn.com.insee.corporate.util.AuthenUtil;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/construction")
public class ConstructionAdminController {

    @Autowired
    private ConstructionService constructionService;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/list", produces = {"application/json"})
    public ResponseEntity<BaseResponse> list(@RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam (required = false, defaultValue = "20") int pageSize) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            PageDTO<ConstructionDTO> list = constructionService.getList(page, pageSize);
            List<ConstructionDTO> constructionDTOS = list.getList();
            for (int i = 0; i < constructionDTOS.size(); i++) {
                ConstructionDTO constructionDTO = constructionDTOS.get(i);
                UserDTO userDTO = userService.findById(constructionDTO.getUserId());
                constructionDTO.setUser(userDTO);
            }
            response.setData(list);
        }catch (Exception e) {
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
            ConstructionDTO constructionDTO = constructionService.findById(id);
            UserDTO userDTO = userService.findById(constructionDTO.getUserId());
            constructionDTO.setUser(userDTO);
            response.setData(constructionDTO);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }


    @PostMapping(path = "/update-status", produces = {"application/json"})
    public ResponseEntity<BaseResponse> updateStatus(@RequestBody Map<String, String> dataMap) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try {
            int id = Integer.parseInt(dataMap.get("id"));
            int status = Integer.parseInt(dataMap.get("status"));
            ConstructionStatus enumStatus = ConstructionStatus.findByStatus(status);
            if (enumStatus == null) {
                throw new StatusNotSupportException();
            }
            ConstructionDTO constructionDTO = constructionService.updateStatus(id, enumStatus);
            response.setData(constructionDTO);
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

}
