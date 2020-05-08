package xgl.Intercepter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xgl.mapper.UserMapper;
import xgl.model.User;
import xgl.model.UserExample;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取登录信息
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie c : cookies) {
                if (c.getName().equals("token")) {
                    String token = c.getValue();
                    UserExample example=new UserExample();
                    example.createCriteria().andTokenEqualTo(token);//条件查询！
                    List<User> users = userMapper.selectByExample(example);
                    if (users.size() != 0) {
                        request.getSession().setAttribute("user", users.get(0));
                        break;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
