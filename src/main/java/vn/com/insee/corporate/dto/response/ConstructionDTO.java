package vn.com.insee.corporate.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Type;
import org.json.JSONObject;
import vn.com.insee.corporate.dto.response.ext.ExtraDTO;

import javax.persistence.Column;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConstructionDTO {
    private Integer id;
    private int city;
    private String district;
    private String address;
    private String name;
    private String phone;
    private int quantity;
    private List<BillDTO> bills;
    private List<ImageDTO> images;
    private int estimateTimeStart;
    private int typeConstruction;
    private int type;
    private int userId;
    private int status;
    private UserDTO user;
    private ExtraDTO extra;
    private LabelDTO label;
    private Integer promotionId;
    private Integer giftId;
    private Long createdTime;
    private Long updatedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<BillDTO> getBills() {
        return bills;
    }

    public void setBills(List<BillDTO> bills) {
        this.bills = bills;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }

    public int getEstimateTimeStart() {
        return estimateTimeStart;
    }

    public void setEstimateTimeStart(int estimateTimeStart) {
        this.estimateTimeStart = estimateTimeStart;
    }

    public int getTypeConstruction() {
        return typeConstruction;
    }

    public void setTypeConstruction(int typeConstruction) {
        this.typeConstruction = typeConstruction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ExtraDTO getExtra() {
        return extra;
    }

    public void setExtra(ExtraDTO extra) {
        this.extra = extra;
    }

    public LabelDTO getLabel() {
        return label;
    }

    public void setLabel(LabelDTO label) {
        this.label = label;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }
}
