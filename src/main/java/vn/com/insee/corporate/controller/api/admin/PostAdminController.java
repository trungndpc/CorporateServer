package vn.com.insee.corporate.controller.api.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.PromotionForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.PromotionDTO;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.PromotionService;

@RestController
@RequestMapping("/api/admin/post")
public class PostAdminController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<BaseResponse> get(@RequestParam(required = true) int id) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            PromotionDTO promotionDTO = promotionService.get(id);
            if (promotionDTO == null) {
                response.setError(ErrorCode.NOT_EXITS);
            }else{
                response.setData(promotionDTO);
            }
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/list", produces = {"application/json"})
    public ResponseEntity<BaseResponse> list(@RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam (required = false, defaultValue = "20") int pageSize) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            PageDTO<PromotionDTO> list = promotionService.getListForAdmin(page, pageSize);
            response.setData(list);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/publish", produces = {"application/json"})
    public ResponseEntity<BaseResponse> publish(@RequestParam(required = false)  int id) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            boolean is = promotionService.publish(id);
            if (!is) {
                response.setError(ErrorCode.FAILED);
            }
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }


    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BaseResponse> create(@RequestBody PromotionForm form) {
        BaseResponse response = new BaseResponse();
        try{
            int id = 0;
            if (form.getId() != null) {
                id = promotionService.update(form.getId(),form);
            }else {
                id = promotionService.create(form);
            }
            response.setError(ErrorCode.SUCCESS);
            response.setData(id);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }



}
