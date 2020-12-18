package vn.com.insee.corporate.exception;

public class CustomerExitException extends Exception{

    public CustomerExitException() {
        super("phone is registered");
    }
}
