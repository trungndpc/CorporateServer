package vn.com.insee.corporate.common;

public enum TypeGift {
    CARD_PHONE(1);
    private int type;

    TypeGift(int type) {
        this.type = type;
    }

    public static TypeGift findByValue(int type) {
        return type == 1 ? CARD_PHONE : null;
    }

    public int getType() {
        return type;
    }
}
