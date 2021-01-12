package vn.com.insee.corporate.common;

public enum NetworkBrand {
    VIETTEL(1), VINAPHONE(2), MOBIPHONE(3);

    private Integer id;

    NetworkBrand(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
