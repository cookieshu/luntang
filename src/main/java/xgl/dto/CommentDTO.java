package xgl.dto;

import lombok.Data;
import xgl.model.User;

@Data   //传到问题页面的评论列表
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Long commentCount;
    private String content;
    private User user;
}
