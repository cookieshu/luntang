package xgl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import xgl.dto.CommentCreateDTO;
import xgl.dto.ResultDTO;
import xgl.model.Comment;
import xgl.model.User;
import xgl.service.CommentService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){//@RequestBody 接收对象
        //评论前需要登录
        User user=(User)request.getSession().getAttribute("user");
        if (user==null){
            return ResultDTO.errorOf(2003,"未登录不能评论，请先登录！");
        }

        Comment comment=new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(1L);
        comment.setLikeCount(0L);
        commentService.insert(comment);//增加评论
       //请求成功！
        return ResultDTO.okOf();
    }

}
