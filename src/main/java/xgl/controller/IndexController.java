package xgl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xgl.dto.PaginationDTO;
import xgl.dto.QuestionDTO;
import xgl.mapper.UserMapper;
import xgl.model.User;
import xgl.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "4")Integer size,
                        @RequestParam(name = "search",required = false)String search
    ) {
        //根据page和size查询数据
        PaginationDTO pagination=questionService.list(search,page,size);
        //把该页的数据放到前端
        model.addAttribute("pagination",pagination);
        model.addAttribute("search",search);
        return "index";
    }
}
