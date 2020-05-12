package xgl.dto;

import lombok.Data;
import xgl.model.User;

@Data
//比queastion多了一个User！
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private long gmtCreate;
    private long gmtModified;
    private Long creator;
    private Long viewCount;
    private Long commentCount;
    private Long likeCount;
    //新增加的user
    private User user;
}
