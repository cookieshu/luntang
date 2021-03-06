/*提交回复*/
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId,1,content);
}
/*提交评论的方法*/
function comment2target(targetId,type,content) {
    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment", /*要提交到的地址*/
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                // $("#comment_section").hide();隐藏
                window.location.reload();//重新加载，刷新！
            } else {
                if (response.code == 2003) {//未登录
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        //打开授权登录窗口
                        window.open("https://github.com/login/oauth/authorize?client_id=Iv1.125b50f58e0c91ac&redirect_uri=http://localhost:8080/callback&scope=user&state=1")
                        window.localStorage.setItem("closable", true);//登陆后关闭上面这个页面
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });

}
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content=$("#input-"+commentId).val();/*获取输入框内容！*/
    comment2target(commentId,2,content);
}

/*展开二级评论*/
function collapseComment(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    //获取二级评论区的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        //展开二级评论区
        $.getJSON("/comment/"+id,function (data) {
            var subCommentContainer=$("#comment-"+id);
            $.each(data.data.reverse(),function (index,comment) {
                var mediaLeftElement=$("<div/>",{
                    "class":"media-left"
                }).append($("<img/>",{
                    "class":"media-object img-rounded",
                    "src":comment.user.avatarUrl
                }));

                var mediaBodyElement=$("<div/>",{
                    "class":"media-body"
                }).append($("<h5/>",{
                    "class":"media-heading",
                    "html":comment.user.name
                })).append($("<div/>",{
                    "html":comment.content
                })).append($("<div/>",{
                    "class":"menu"
                }).append($("<span/>",{
                    "class":"pull-right",
                    "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                })));

                var mediaElement=$("<div/>",{
                    "class":"media"
                }).append(mediaLeftElement).append(mediaBodyElement);

               var commentElement=$("<div/>",{
                   "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
               }).append(mediaElement);
               subCommentContainer.prepend(commentElement);
            });

            //展开二级评论
            comments.addClass("in");
            //标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");//添加属性标签！
        });
    }
}
//显示标签框
/*function showSelectTag() {
    $("select-tag").show();失效，和luntang.css的 display: none;  冲突！
}*/
/*选择标签*/
function selectTag(e){
    var value=e.getAttribute("data-tag");
    var previous=$("#tag").val();//先获取原有的值
    if(previous.indexOf(value)==-1){//先判断value标签是否存在
        if(previous){//previous不为空null
            $("#tag").val(previous+','+value);
        }else {
            $("#tag").val(value);
        }
    }
}