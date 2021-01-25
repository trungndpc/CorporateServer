package vn.com.insee.corporate.common;

public class MessageManager {
    private static final String TMP_REGISTER_SUCCESSFUL  = "Chúc mừng anh %s! Hồ sơ nhà thầu của anh đã được phê duyệt thành công! Hãy tham gia ngay các chương trình khuyến mãi độc quyền dành cho thành viên bằng cách nhấn vào nút khuyến mãi bên dưới. Mọi thắc mắc xin liên hệ hotline 1800 1718.";
    private static final String TMP_REGISTER_FAILED = "Hồ sơ đăng ký nhà thầu ngoại hạng không thành công. Lý do: %s. Mọi thắc mắc xin liên hệ hotline: 1800 1718";
    private static final String TMP_AFTER_REGISTER_PROMOTION  = "Đơn tham gia chương trình khuyến mãi %s đã được gửi thành công! INSEE sẽ kiểm tra và thông báo kết quả duyệt đơn trong thời gian 3-5 ngày. Mọi thắc mắc xin liên hệ hotline 1800 1718.";
    private static final String TMP_APPROVED_PROMOTION = "Đơn tham gia chương trình khuyến mãi %s đã được xác nhận thành công. Quà khuyến mãi sẽ được INSEE gửi trong vòng 3-5 ngày qua tin nhắn trên Zalo. Mọi thắc mắc xin liên hệ hotline: 1800 1718";
    private static final String TMP_REJECTED_PROMOTION = "Đơn tham gia chương trình khuyến mãi %s không được xác nhận thành công. Lý do: %s.Mọi thắc mắc xin liên hệ hotline 18001718";
    private static final String TMP_SEND_GIFT_PROMOTION = "Chúc mừng anh %s đã nhận được thẻ điện thoại %s từ chương trình khuyến mãi của câu lạc bộ INSEE Nhà Thầu Ngoại Hạng. Vui lòng bấm vào tin nhắn để nhận được phần thưởng.";

    public static String getMsgRegisterSuccessful(String name) {
        return String.format(TMP_REGISTER_SUCCESSFUL, name);
    }

    public static String getMsgRegisterFailed(String note) {
        return String.format(TMP_REGISTER_FAILED, note);
    }

    public static String getMsgAfterRegisterPromotion(String titlePromotion) {
        return String.format(TMP_AFTER_REGISTER_PROMOTION, titlePromotion);
    }

    public static String getMsgApprovedPromotion(String titlePromotion) {
        return String.format(TMP_APPROVED_PROMOTION, titlePromotion);
    }

    public static String getMsgRejectedPromotion(String titlePromotion, String note) {
        return String.format(TMP_REJECTED_PROMOTION, titlePromotion, note);
    }

    public static String getMsgSendGiftPromotion(String name, String gift) {
        return String.format(TMP_SEND_GIFT_PROMOTION, name, gift);
    }




}
