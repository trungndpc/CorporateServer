package vn.com.insee.corporate.common.type;

public enum  TypeConstruction {
    NEXT_CONSTRUCTION(1), NOW_CONSTRUCTION(2);
    private int type;

    TypeConstruction(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static TypeConstruction findByType(int type) {
        switch (type) {
            case 1 : return NEXT_CONSTRUCTION;
            case 2 : return NOW_CONSTRUCTION;
            default: return null;
        }
    }
}
