package xgl.exception;

//一个接口，不同的异常信息进行实现！
public interface ICustomizeErrorCode {
    //异常提示信息
    String getMessage();
    //错误代码
    Integer getCode();
}
