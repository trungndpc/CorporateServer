package vn.com.insee.corporate.entity;

import javax.persistence.*;

@Entity
@Table(name = "bill", schema="insee_promotion")
public class BillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String url;
    private int status;
    private int constructionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
