package vn.com.insee.corporate.controller.api.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.response.admin.report.DashboardDTO;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.StatisticalService;


@RestController
@RequestMapping("/api/admin/statistical")
public class StatisticalController {

    @Autowired
    private StatisticalService statisticalService;

    @GetMapping(path = "/dashboard", produces = {"application/json"})
    public ResponseEntity<BaseResponse> history() {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            DashboardDTO dashboard = statisticalService.getDashboard();
            response.setData(dashboard);
        }catch (Exception e) {
            e.printStackTrace();
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }

}
