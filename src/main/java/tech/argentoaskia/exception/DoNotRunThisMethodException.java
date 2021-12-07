package tech.argentoaskia.exception;

import java.io.IOException;

public class DoNotRunThisMethodException extends IOException {
    public DoNotRunThisMethodException(){
        super();
    }
    public DoNotRunThisMethodException(Throwable cause){
        super(cause);
    }
    public DoNotRunThisMethodException(String msg){
        super(msg);
    }
    public DoNotRunThisMethodException(String msg, Throwable cause){
        super(msg, cause);
    }
}
