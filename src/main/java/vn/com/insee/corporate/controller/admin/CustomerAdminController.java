package vn.com.insee.corporate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.com.insee.corporate.common.CustomerStatusEnum;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.RegisterForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.exception.StatusNotSupportException;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.CustomerService;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/post")
public class CustomerAdminController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(path = "/list", produces = {"application/json"})
    public ResponseEntity<BaseResponse> list(@RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam (required = false, defaultValue = "20") int pageSize) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            PageDTO<CustomerDTO> list = customerService.getList(page, pageSize);
            response.setData(list);
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/import", produces = {"application/json"})
    public ResponseEntity<BaseResponse> create(@RequestBody RegisterForm form, Authentication authentication) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            CustomerDTO customerDTO = customerService.createByAdmin(form);
            response.setData(customerDTO);
        }catch (Exception e) {
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
            CustomerStatusEnum enumStatus = CustomerStatusEnum.findByStatus(status);
            if (enumStatus == null) {
                throw new StatusNotSupportException();
            }
            CustomerDTO customerDTO = customerService.updateStatus(id, enumStatus);
            response.setData(customerDTO);
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }
}
