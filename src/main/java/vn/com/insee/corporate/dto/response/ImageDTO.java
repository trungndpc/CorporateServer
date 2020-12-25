package vn.com.insee.corporate.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDTO {
    private int id;
    private String link;
    private int status;
    private int constructionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(int constructionId) {
        this.constructionId = constructionId;
    }
}
