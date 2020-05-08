package xgl.dto;

import lombok.Data;
import xgl.model.User;

@Data
//比queastion多了一个User！
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private long gmtCreate;
    private long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    //新增加的user
    private User user;
}
