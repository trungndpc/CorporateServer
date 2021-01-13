package vn.com.insee.corporate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.BillStatus;
import vn.com.insee.corporate.common.ConstructionStatus;
import vn.com.insee.corporate.common.ImageStatus;
import vn.com.insee.corporate.dto.ConstructionForm;
import vn.com.insee.corporate.dto.page.PageDTO;
import vn.com.insee.corporate.dto.response.*;
import vn.com.insee.corporate.entity.*;
import vn.com.insee.corporate.exception.ConstructionExitException;
import vn.com.insee.corporate.exception.CustomerExitException;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.*;
import vn.com.insee.corporate.service.external.ZaloService;

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

    @Autowired
    private ZaloService zaloService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public ConstructionDTO create(ConstructionForm form, int userId) {
        ConstructionEntity constructionEntity = new ConstructionEntity();
        mapper.map(form, constructionEntity);
        constructionEntity.setUserId(userId);
        constructionEntity.setStatus(ConstructionStatus.WAITING_APPROVAL.getStatus());
        JSONObject extra = form.getExtra();
        if (extra != null) {
            constructionEntity.setExtra(extra.toString());
        }
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

    public ConstructionDTO findById(int id) throws ConstructionExitException {
        Optional<ConstructionEntity> constructionEntity = constructionRepository.findById(id);
        if (!constructionEntity.isPresent()) {
            throw new ConstructionExitException();
        }
        ConstructionDTO constructionDTO = new ConstructionDTO();
        List<BillDTO> billDTOS = null;
        List<ImageDTO> imageDTOS = null;
        mapper.map(constructionEntity.get(), constructionDTO);
        if (constructionEntity.get().getExtra() != null) {
            constructionDTO.setExtra(new JSONObject(constructionEntity.get().getExtra()));
        }

        List<Integer> billIds = constructionEntity.get().getBillIds();
        if (billIds != null && billIds.size() > 0) {
            List<BillEntity> billEntities = billRepository.findAllById(billIds);
            if (billEntities != null && billEntities.size() > 0) {
                billDTOS = new ArrayList<>();
                for (BillEntity e: billEntities) {
                    BillDTO billDTO = new BillDTO();
                    mapper.map(e, billDTO);
                    billDTOS.add(billDTO);
                }
            }
        }
        List<Integer> imageIds = constructionEntity.get().getImageIds();
        if (imageIds != null && imageIds.size() > 0) {
            List<ImageEntity> imageEntities = imageRepository.findAllById(imageIds);
            if (imageEntities != null && imageEntities.size() > 0) {
                imageDTOS = new ArrayList<>();
                for (ImageEntity e: imageEntities) {
                    ImageDTO imageDTO = new ImageDTO();
                    mapper.map(e, imageDTO);
                    imageDTOS.add(imageDTO);
                }
            }
        }
        Integer labelId = constructionEntity.get().getLabelId();
        if (labelId != null) {
            Optional<LabelEntity> optionalLabelEntity = labelRepository.findById(labelId);
            if (optionalLabelEntity.isPresent()) {
                constructionDTO.setLabel(mapper.map(optionalLabelEntity.get(), LabelDTO.class));
            }
        }

        constructionDTO.setBills(billDTOS);
        constructionDTO.setImages(imageDTOS);
        return constructionDTO;
    }

    public PageDTO<ConstructionDTO> getList(int type, Integer status, int page, int pageSize) {
        Pageable pageable =
                PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<ConstructionEntity> constructionEntities;
        if (status == null) {
            constructionEntities = constructionRepository.findByType(type, pageable);
        }else {
            constructionEntities = constructionRepository.findByTypeAndStatus(type, status, pageable);
        }
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

    public ConstructionDTO updateStatus(int id, ConstructionStatus status) throws ConstructionExitException, JsonProcessingException {
        Optional<ConstructionEntity> optionalConstructionEntity = constructionRepository.findById(id);
        if (!optionalConstructionEntity.isPresent()) {
            throw new ConstructionExitException();
        }

        if (optionalConstructionEntity.get().getStatus() != status.getStatus()) {
            int userId = optionalConstructionEntity.get().getUserId();
            UserEntity userEntity = userRepository.getOne(userId);

            if (status == ConstructionStatus.APPROVED) {
                List<Integer> billIds = optionalConstructionEntity.get().getBillIds();
                if (billIds != null && billIds.size() > 0) {
                    List<BillEntity> billEntities = billRepository.findAllById(billIds);
                    int volumeCiment = 0;
                    for (BillEntity billEntity: billEntities) {
                        if (billEntity.getStatus() == BillStatus.APPROVED.getStatus()) {
                            if (billEntity.getVolumeCiment() != null) {
                                volumeCiment = volumeCiment + billEntity.getVolumeCiment();
                            }
                        }
                    }
                    if (userEntity != null) {
                        Integer customerId = userEntity.getCustomerId();
                        CustomerEntity customerEntity = customerRepository.getOne(customerId);
                        customerEntity.setVolumeCiment(customerEntity.getVolumeCiment() + volumeCiment);
                        customerRepository.saveAndFlush(customerEntity);
                    }


                }
            }

            if (userEntity != null && userEntity.getFollowerZaloId() != null)
            if (status == ConstructionStatus.APPROVED) {
                zaloService.sendTextMsg(userEntity.getFollowerZaloId(), "Công trình của bạn đã được chúng tôi xác thực! Trạng thái hóa đơn, hình ảnh của bạn upload sẽ được chúng tôi cập nhật trên trang nhà thầu chính thức của INSEE.");
            }else if(status == ConstructionStatus.REJECTED) {
                zaloService.sendTextMsg(userEntity.getFollowerZaloId(), "Rất tiếc!!! Những thông tin công trình của bạn cung cấp không đáp ứng được yêu cầu của chúng tôi!!!");
            }
        }
        optionalConstructionEntity.get().setStatus(status.getStatus());
        ConstructionEntity constructionEntity = constructionRepository.saveAndFlush(optionalConstructionEntity.get());
        ConstructionDTO constructionDTO = new ConstructionDTO();
        mapper.map(constructionEntity, constructionDTO);
        return constructionDTO;
    }

    public boolean updateLabel(int id, int labelId) throws CustomerExitException {
        Optional<ConstructionEntity> constructionEntityOptional = constructionRepository.findById(id);
        if (!constructionEntityOptional.isPresent()) {
            throw new CustomerExitException();
        }
        ConstructionEntity constructionEntity = constructionEntityOptional.get();
        constructionEntity.setLabelId(labelId);
        constructionRepository.saveAndFlush(constructionEntity);
        return true;
    }

}
