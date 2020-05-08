package xgl.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import xgl.exception.CustomizeException;

//自定义的异常控制类
@ControllerAdvice
public class CustomizeExceptionHander {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model) {
        if (e instanceof CustomizeException) {
            model.addAttribute("message", e.getMessage());
        } else {
            model.addAttribute("message", "服务器繁忙，请稍后再试");
        }
        return new ModelAndView("error");
    }
}
