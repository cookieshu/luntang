package xgl.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xgl.dto.PaginationDTO;
import xgl.dto.QuestionDTO;
import xgl.exception.CustomizeErrorCode;
import xgl.exception.CustomizeException;
import xgl.mapper.QuestionMapper;
import xgl.mapper.UserMapper;
import xgl.model.Question;
import xgl.model.QuestionExample;
import xgl.model.User;
import xgl.model.UserExample;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    //查询所有的问题
    public PaginationDTO list(Integer page, Integer size) {
        //返回的是page信息
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        Integer totalPage;//总页数
        //计算页数
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //防止错误的page
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        //设置paginationDTO
        paginationDTO.setPagination(totalPage, page);
        //offset是该页面开始的条数
        Integer offset = size * (page - 1);
        //根据offset和size查询内容
        //mybatis的分页查询！
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //questions组装user
        for (Question question : questions) {
            //查询
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //赋值！
            BeanUtils.copyProperties(question, questionDTO);
            //设置user
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        //把问题放入page
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    //查询用户的问题
    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        //返回的是page信息
        PaginationDTO paginationDTO = new PaginationDTO();
        QuestionExample questionExample=new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);
        Integer totalPage;//总页数
        //计算页数
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //防止错误的page
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        //设置paginationDTO
        paginationDTO.setPagination(totalPage, page);
        //offset是该页面开始的条数
        Integer offset = size * (page - 1);
        //根据offset和size查询内容
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //questions组装user
        for (Question question : questions) {
            UserExample example=new UserExample();
            example.createCriteria().andIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(example);
            User user = users.get(0);
            QuestionDTO questionDTO = new QuestionDTO();
            //赋值！
            BeanUtils.copyProperties(question, questionDTO);
            //设置user
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        //把问题放入page
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        //赋值！
        BeanUtils.copyProperties(question, questionDTO);
        UserExample example=new UserExample();
        example.createCriteria().andIdEqualTo(question.getCreator());
        List<User> users = userMapper.selectByExample(example);
        questionDTO.setUser(users.get(0));
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else {
            //更新
            Question updateQuestion=new Question();
            updateQuestion.setGmtModified(question.getGmtCreate());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example=new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExample(updateQuestion, example);
            if (updated!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }
}
