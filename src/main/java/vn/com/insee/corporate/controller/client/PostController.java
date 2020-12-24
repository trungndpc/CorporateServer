package vn.com.insee.corporate.controller.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.insee.corporate.constant.ErrorCode;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.PostDTO;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.service.PostService;

@RestController
@RequestMapping("/api/customer")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping(path = "/list", produces = {"application/json"})
    public ResponseEntity<BaseResponse> list(@RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam (required = false, defaultValue = "20") int pageSize) {
        BaseResponse response = new BaseResponse(ErrorCode.SUCCESS);
        try{
            PageDTO<PostDTO> list = postService.getList(page, pageSize);
            response.setData(list);
        }catch (Exception e) {
            response.setError(ErrorCode.FAILED);
        }
        return ResponseEntity.ok(response);
    }
}
