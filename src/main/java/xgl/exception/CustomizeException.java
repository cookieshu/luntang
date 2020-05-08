package xgl.exception;

public class CustomizeException extends RuntimeException {
    private String message;

    /**
     * 接收异常信息
     * @param errorCode
     */
    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message=errorCode.getMessage();
    }

    /**
     * 构造函数
     * @param message
     */
    public CustomizeException(String message){
        this.message=message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
