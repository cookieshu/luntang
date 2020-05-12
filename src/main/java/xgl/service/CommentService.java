package xgl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xgl.enums.CommentTypeEnum;
import xgl.exception.CustomizeErrorCode;
import xgl.exception.CustomizeException;
import xgl.mapper.CommentMapper;
import xgl.mapper.QuestionExtMapper;
import xgl.mapper.QuestionMapper;
import xgl.model.Comment;
import xgl.model.Question;
import xgl.model.QuestionExample;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Transactional //事务管理
    public void insert(Comment comment) {
        //处理评论时可能出现的异常(在服务层)
        if (comment.getParentId()==null || comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType()==CommentTypeEnum.COMMRNT.getType()){//回复评论
            //找到评论
            Comment dbcomment = commentMapper.selectByPrimaryKey(comment.getId());
            if (dbcomment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);//插入
        }else {//回复问题
            //找到该问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1L);//递增，加1
            questionExtMapper.incCommentCount(question);

        }

    }
}
