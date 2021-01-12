package vn.com.insee.corporate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.common.TypeLabel;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.response.LabelDTO;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.LabelService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/label")
public class LabelAdminController {

    @Autowired
    private LabelService labelService;

    @GetMapping(path = "/list", produces = {"application/json"})
    public ResponseEntity<BaseResponse> list() {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            List<LabelDTO> labelDTOS = labelService.findByType(TypeLabel.CONSTRUCTION);
            response.setData(labelDTOS);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }
}
