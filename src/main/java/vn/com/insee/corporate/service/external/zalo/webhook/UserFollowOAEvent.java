package vn.com.insee.corporate.service.external.zalo.webhook;

public class UserFollowOAEvent extends ZEvent{
    public String source;
    public Follower follower;

    public static class Follower {
        public String id;
    }
}
