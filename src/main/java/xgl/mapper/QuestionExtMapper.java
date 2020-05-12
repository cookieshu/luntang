package xgl.mapper;

import xgl.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
}
