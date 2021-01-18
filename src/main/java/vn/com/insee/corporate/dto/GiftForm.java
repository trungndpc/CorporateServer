package vn.com.insee.corporate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.com.insee.corporate.dto.response.ext.PhoneCard;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GiftForm {
    private Integer id;
    private String name;
    private int type;
    private int constructionId;
    private int customerId;
    private int network;
    private int status;
    private List<PhoneCard> cards;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(int constructionId) {
        this.constructionId = constructionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<PhoneCard> getCards() {
        return cards;
    }

    public void setCards(List<PhoneCard> cards) {
        this.cards = cards;
    }

    public int getNetwork() {
        return network;
    }

    public void setNetwork(int network) {
        this.network = network;
    }
}
