package vn.com.insee.corporate.dto.response.client;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PromotionClientDTO {
    private Integer id;
    private String title;
    private String subTitle;
    private String content;
    private int typePromotion;
    private String summary;
    private Integer status;
    private Integer location;
    private Long timeStart;
    private Long timeEnd;
    private Long time;
    private Integer playingStatus;
    private List<Integer> listPlayingId;

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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getPlayingStatus() {
        return playingStatus;
    }

    public void setPlayingStatus(Integer playingStatus) {
        this.playingStatus = playingStatus;
    }

    public List<Integer> getListPlayingId() {
        return listPlayingId;
    }

    public void setListPlayingId(List<Integer> listPlayingId) {
        this.listPlayingId = listPlayingId;
    }
}
