package xgl.enums;

//评论的类型：一级，二级
public enum CommentTypeEnum {
    //下面两个是 CommentTypeEnum的实例对象，所以需要对应的构造函数
    QUESTION(1),//问题的评论
    COMMRNT(2);//评论的评论

    private Integer type;

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        //遍历枚举类
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
