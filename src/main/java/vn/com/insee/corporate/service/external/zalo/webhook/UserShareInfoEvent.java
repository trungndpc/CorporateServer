package vn.com.insee.corporate.service.external.zalo.webhook;

public class UserShareInfoEvent extends ZEvent{
    public Info info;

    private static class Info {
        public String address;
        public String phone;
        public String city;
        public String district;
        public String name;
        public String ward;
    }
}
