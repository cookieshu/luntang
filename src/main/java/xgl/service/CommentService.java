package xgl.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xgl.dto.CommentDTO;
import xgl.enums.CommentTypeEnum;
import xgl.exception.CustomizeErrorCode;
import xgl.exception.CustomizeException;
import xgl.mapper.CommentMapper;
import xgl.mapper.QuestionExtMapper;
import xgl.mapper.QuestionMapper;
import xgl.mapper.UserMapper;
import xgl.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional //事务管理
    public void insert(Comment comment) {
        //处理评论时可能出现的异常(在服务层)
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMRNT.getType()) {//回复评论
            //找到评论
            Comment dbcomment = commentMapper.selectByPrimaryKey(comment.getId());
            if (dbcomment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);//插入
        } else {//回复问题
            //找到该问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1L);//递增，加1
            questionExtMapper.incCommentCount(question);

        }

    }

    public List<CommentDTO> listByQuestionId(Long id) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)  //根据问题的id
                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());//确定类型为问题
        commentExample.setOrderByClause("gmt_create desc");//设置排序
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        //获取评论人，注意避免重复
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> useIds = new ArrayList<>();
        useIds.addAll(commentators);

        //获取评论人 并转为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(useIds);//In !!!
        List<User> users = userMapper.selectByExample(userExample);
        //装换为Mapper，便于后期查找
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换commnet为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(
                comment -> {
                    CommentDTO commentDTO = new CommentDTO();
                    BeanUtils.copyProperties(comment, commentDTO);
                    //设置评论人
                    commentDTO.setUser(userMap.get(comment.getCommentator()));
                    return commentDTO;
                }).collect(Collectors.toList());
        return commentDTOS;
    }
}
