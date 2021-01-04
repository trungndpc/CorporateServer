package vn.com.insee.corporate.service;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.BillStatus;
import vn.com.insee.corporate.common.ConstructionStatus;
import vn.com.insee.corporate.common.ImageStatus;
import vn.com.insee.corporate.dto.ConstructionForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.BillDTO;
import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.dto.response.ImageDTO;
import vn.com.insee.corporate.entity.BillEntity;
import vn.com.insee.corporate.entity.ConstructionEntity;
import vn.com.insee.corporate.entity.ImageEntity;
import vn.com.insee.corporate.exception.ConstructionExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.BillRepository;
import vn.com.insee.corporate.repository.ConstructionRepository;
import vn.com.insee.corporate.repository.ImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConstructionService {

    @Autowired
    Mapper mapper;

    @Autowired
    private ConstructionRepository constructionRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private BillRepository billRepository;

    public ConstructionDTO create(ConstructionForm form, int userId) {
        ConstructionEntity constructionEntity = new ConstructionEntity();
        mapper.map(form, constructionEntity);
        constructionEntity.setUserId(userId);
        constructionEntity.setStatus(ConstructionStatus.WAITING_APPROVAL.getStatus());
        List<String> billIds = form.getBillIds();
        List<BillEntity> billEntities = new ArrayList<>();
        if (billIds != null) {
            for (String billUrl : billIds) {
                BillEntity billEntity = new BillEntity();
                billEntity.setLink(billUrl);
                billEntity.setStatus(BillStatus.WAITING_APPROVAL.getStatus());
                billEntities.add(billEntity);
            }
            billEntities = billRepository.saveAll(billEntities);
            constructionEntity.setBillIds(billEntities.stream().map(b -> b.getId()).collect(Collectors.toList()));
        }

        List<String> imageIds = form.getImageIds();
        List<ImageEntity> imageEntities = new ArrayList<>();
        if (imageIds != null) {
            for (String imageUrl : imageIds) {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setLink(imageUrl);
                imageEntity.setStatus(ImageStatus.WAITING_APPROVAL.getStatus());
                imageEntities.add(imageEntity);
            }
            imageEntities = imageRepository.saveAll(imageEntities);
            constructionEntity.setImageIds(imageEntities.stream().map(i -> i.getId()).collect(Collectors.toList()));
        }
        constructionEntity = constructionRepository.saveAndFlush(constructionEntity);
        ConstructionDTO construction = new ConstructionDTO();
        mapper.map(constructionEntity, construction);

        if (billEntities != null && billEntities.size() > 0) {
            List<BillDTO> billDTOS = new ArrayList<>();
            for (BillEntity bill : billEntities) {
                bill.setConstructionId(constructionEntity.getId());
                BillDTO billDTO = new BillDTO();
                mapper.map(bill, billDTO);
                billDTOS.add(billDTO);
            }
            billRepository.saveAll(billEntities);
            construction.setBills(billDTOS);
        }

        if (imageEntities != null && imageEntities.size() > 0) {
            List<ImageDTO> imageDTOS = new ArrayList<>();
            for (ImageEntity imageEntity : imageEntities) {
                imageEntity.setConstructionId(constructionEntity.getId());
                ImageDTO imageDTO = new ImageDTO();
                mapper.map(imageEntity, imageDTO);
                imageDTOS.add(imageDTO);
            }
            imageRepository.saveAll(imageEntities);
            construction.setImages(imageDTOS);
        }
        return construction;
    }

    public PageDTO<ConstructionDTO> getList(int page, int pageSize) {
        Page<ConstructionEntity> constructionEntities = constructionRepository.findAll(PageRequest.of(page, pageSize));
        List<ConstructionDTO> constructionDTOS = mapper.mapToList(constructionEntities.toList(), new TypeToken<List<ConstructionDTO>>() {
        }.getType());
        PageDTO<ConstructionDTO> pageData = new PageDTO<ConstructionDTO>(page, pageSize, constructionEntities.getTotalPages(), constructionDTOS);
        return pageData;
    }

    public List<ConstructionDTO> findByUserId(int userId) {
        List<ConstructionEntity> constructionEntityList = constructionRepository.findByUserId(userId);
        List<ConstructionDTO> constructionDTOS = mapper.mapToList(constructionEntityList, new TypeToken<List<ConstructionDTO>>() {
        }.getType());
        return constructionDTOS;
    }

    public ConstructionDTO updateStatus(int id, ConstructionStatus status) throws ConstructionExitException {
        Optional<ConstructionEntity> optionalConstructionEntity = constructionRepository.findById(id);
        if (!optionalConstructionEntity.isPresent()) {
            throw new ConstructionExitException();
        }
        optionalConstructionEntity.get().setStatus(status.getStatus());
        ConstructionEntity constructionEntity = constructionRepository.saveAndFlush(optionalConstructionEntity.get());
        ConstructionDTO constructionDTO = new ConstructionDTO();
        mapper.map(constructionEntity, constructionDTO);
        return constructionDTO;
    }


}
