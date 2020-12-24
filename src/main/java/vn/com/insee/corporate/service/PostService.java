package vn.com.insee.corporate.service;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.PostStatusEnum;
import vn.com.insee.corporate.dto.PostForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.PostDTO;
import vn.com.insee.corporate.entity.PromotionEntity;
import vn.com.insee.corporate.exception.PostNotExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Mapper mapper;

    public PageDTO<PostDTO> getList(int page, int pageSize) {
        Page<PromotionEntity> postEntities = postRepository.findAll(PageRequest.of(page, pageSize));
        List<PostDTO> postDTOList = mapper.mapToList(postEntities.toList(), new TypeToken<List<PostDTO>>() {
        }.getType());
        PageDTO<PostDTO> pageData = new PageDTO<PostDTO>(page, pageSize, postEntities.getTotalPages(), postDTOList);
        return pageData;
    }

    public PostDTO get(int id) throws PostNotExitException {
        Optional<PromotionEntity> postEntity = postRepository.findById(id);
        if (!postEntity.isPresent()) {
            throw new PostNotExitException();
        }
        return mapper.map(postEntity.get(), PostDTO.class);
    }

    public PostDTO create(PostForm postForm) {
        PromotionEntity postEntity = new PromotionEntity();
        mapper.map(postForm, postEntity);
        postEntity.setStatus(PostStatusEnum.INIT.getStatus());
        postEntity = postRepository.saveAndFlush(postEntity);
        PostDTO postDTO = new PostDTO();
        mapper.map(postEntity, postDTO);
        return  postDTO;
    }

    public boolean publish(int id) throws PostNotExitException {
        Optional<PromotionEntity> postEntity = postRepository.findById(id);
        if (!postEntity.isPresent()) {
            throw new PostNotExitException();
        }
        postEntity.get().setStatus(PostStatusEnum.PUBLISHED.getStatus());
        postRepository.saveAndFlush(postEntity.get());
        return true;
    }

}
