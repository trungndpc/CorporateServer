package vn.com.insee.corporate.entity;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "promotion", schema="promotion")
@TypeDef(name = "list-array",typeClass = ListArrayType.class)
public class PromotionEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String cover;
    private String content;
    private int typePromotion;
    private String summary;
    private Integer status;
    private Long timeStart;
    private Long timeEnd;
    private Integer ruleQuantily;

    @Type(type = "list-array")
    @Column(name = "rule_accepted_cement",columnDefinition = "integer[]")
    private List<Integer> ruleAcceptedCement;

    @Type(type = "list-array")
    @Column(name = "location",columnDefinition = "integer[]")
    private List<Integer> location;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTypePromotion() {
        return typePromotion;
    }

    public void setTypePromotion(int typePromotion) {
        this.typePromotion = typePromotion;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getLocation() {
        return location;
    }

    public void setLocation(List<Integer> location) {
        this.location = location;
    }

    public Long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Long timeStart) {
        this.timeStart = timeStart;
    }

    public Long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getRuleQuantily() {
        return ruleQuantily;
    }

    public void setRuleQuantily(Integer ruleQuantily) {
        this.ruleQuantily = ruleQuantily;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<Integer> getRuleAcceptedCement() {
        return ruleAcceptedCement;
    }

    public void setRuleAcceptedCement(List<Integer> ruleAcceptedCement) {
        this.ruleAcceptedCement = ruleAcceptedCement;
    }
}
