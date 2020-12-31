package vn.com.insee.corporate.service.external.zalo;

public class TextMessage {
    private Recipient recipient;
    private Message message;

    public TextMessage(String followerId, String text) {
        this.recipient = new Recipient(followerId);
        this.message = new Message();
        this.message.setText(text);
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
