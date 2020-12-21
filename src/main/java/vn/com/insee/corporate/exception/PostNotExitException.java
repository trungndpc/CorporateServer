package vn.com.insee.corporate.exception;

public class PostNotExitException extends Exception{
    public PostNotExitException() {
        super("Post not exits");
    }
}
