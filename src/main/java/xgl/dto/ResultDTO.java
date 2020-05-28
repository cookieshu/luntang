package xgl.dto;

import lombok.Data;
import xgl.exception.CustomizeErrorCode;
import xgl.exception.CustomizeException;

@Data
public class ResultDTO<T> {//处理 评论的结果
    private Integer code;
    private String message;
    private T data;//不确定T是List还是其他

    //评论时出现错误或者异常
    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(), e.getMessage());
    }

    //评论成功
    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    //评论成功
    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }
}
