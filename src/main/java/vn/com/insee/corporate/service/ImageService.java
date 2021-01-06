package vn.com.insee.corporate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.ImageStatus;
import vn.com.insee.corporate.entity.ImageEntity;
import vn.com.insee.corporate.exception.NotExitException;
import vn.com.insee.corporate.repository.ImageRepository;

@Service
public class ImageService {

    @Autowired
    private ImageRepository repository;

    public void updateStatus(int id, ImageStatus imageStatus) throws NotExitException {
        ImageEntity imageEntity = repository.getOne(id);
        if (imageEntity == null) throw new NotExitException();
        imageEntity.setStatus(imageStatus.getStatus());
        repository.save(imageEntity);
    }
}
