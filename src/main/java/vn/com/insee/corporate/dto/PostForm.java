package vn.com.insee.corporate.dto;

public class PostForm {
    private Integer id;
    private String title;
    private String subTitle;
    private String content;
    private int typePromotion;
    private String summary;

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
}
