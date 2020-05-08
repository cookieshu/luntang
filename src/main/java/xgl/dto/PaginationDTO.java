package xgl.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
//传递page分页数据

@Data
public class PaginationDTO {
    //该页面的数据（问题）
    private List<QuestionDTO> questions;
    private boolean showPrevious;//是否上一页
    private boolean showFirstPage;//是否第一页
    private boolean showNext;//下一页
    private boolean showEndPage;//最后一页
    private Integer page;//当前页码
    //必须new出来！后面直接使用
    private List<Integer> pages=new ArrayList<>();//附近页码,妙用！
    private Integer totalPage;//总页数

    //设置page
    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage=totalPage;
        this.page=page;
        //显示附近页码
        pages.add(page);
        for (int i=1;i<=3;i++){
            if (page-i>0){
                //page前面的页面
                pages.add(0,page-i);
            }
            if (page+i<=totalPage){
                //page后面的页面
                pages.add(page+i);
            }
        }
        //是否展示上一页
        if(page==1){
            showPrevious=false;
        }else {
            showPrevious=true;
        }
        //是否展示下一页
        if (page==totalPage){
            showNext=false;
        }else {
            showNext=true;
        }
        //是否展示第一页
        if (pages.contains(1)){
            showFirstPage=false;
        }else {
            showFirstPage=true;
        }
        //是否展示最后一页
        if (pages.contains(totalPage)){
            showEndPage=false;
        }else {
            showEndPage=true;
        }
    }
}
