package xgl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import xgl.dto.PaginationDTO;
import xgl.mapper.UserMapper;
import xgl.model.Notification;
import xgl.model.User;
import xgl.service.NotificationService;
import xgl.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action")String action,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size" ,defaultValue = "5")Integer size,
                          Model model){
        //获取user
        User user = (User)request.getSession().getAttribute("user");
        //如果没有user
        if (user==null){
            return "redirect:/";
        }
        //设置页面的主题！
        if ("questions".equals(action)){
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            model.addAttribute("pagination",paginationDTO);
        }else if("replies".equals(action)){
            PaginationDTO paginationDTO =notificationService.list(user.getId(), page, size);
            Long unreadCount=notificationService.unreadCount(user.getId());
            model.addAttribute("unreadCount",unreadCount);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            model.addAttribute("pagination",paginationDTO);
        }
        //查询到相关的问题
        return "profile";
    }

}
