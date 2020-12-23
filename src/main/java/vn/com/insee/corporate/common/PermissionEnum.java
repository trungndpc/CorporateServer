package vn.com.insee.corporate.common;

public enum  PermissionEnum {

    ADMIN(1, "Admin"),
    CUSTOMER(2, "Customer"),
    EMPLOYEE(3, "Employee"),
    ANONYMOUS(0, "Anonymous");

    private int id;
    private String name;

    PermissionEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static PermissionEnum findById(int id) {
        switch (id) {
            case 1 : return ADMIN;
            case 2 : return CUSTOMER;
            case 3 : return EMPLOYEE;
            default: return ANONYMOUS;
        }
    }

    public static PermissionEnum findByName(String name) {
        switch (name) {
            case "Admin" : return ADMIN;
            case "Customer" : return CUSTOMER;
            case "Employee" : return EMPLOYEE;
            default: return  ANONYMOUS;
        }
    }
}
