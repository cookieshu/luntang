package xgl.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private String search;
    private Integer page;//分页时用的页码
    private Integer size;//分页时用的
}
