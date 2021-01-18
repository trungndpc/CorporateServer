package vn.com.insee.corporate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.BillStatus;
import vn.com.insee.corporate.common.ImageStatus;
import vn.com.insee.corporate.dto.response.BillDTO;
import vn.com.insee.corporate.dto.response.ImageDTO;
import vn.com.insee.corporate.entity.BillEntity;
import vn.com.insee.corporate.entity.ImageEntity;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.ImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

    @Autowired
    private ImageRepository repository;

    @Autowired
    private Mapper mapper;

    public void update(int id, int status) {
        ImageEntity one = repository.getOne(id);
        one.setStatus(status);
        repository.saveAndFlush(one);
    }

    public List<Integer> initFromListUrl(List<String> urls) {
        List<ImageEntity> imageEntities = new ArrayList<>();
        for (String url : urls) {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setLink(url);
            imageEntity.setStatus(ImageStatus.WAITING_APPROVAL.getStatus());
            imageEntities.add(imageEntity);
        }
        imageEntities = repository.saveAll(imageEntities);
        return imageEntities.stream().map(b -> b.getId()).collect(Collectors.toList());
    }

    public List<ImageDTO> getList(List<Integer> ids) {
        List<ImageEntity> entities =  repository.findAllById(ids);
        if (entities != null) {
            return entities.stream().map(e -> mapper.map(e, ImageDTO.class)).collect(Collectors.toList());
        }
        return null;
    }
}
