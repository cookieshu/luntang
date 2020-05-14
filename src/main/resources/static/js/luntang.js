function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    $.ajax({
        type: "POST",
        url: "/comment", /*要提交到的地址*/
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        success: function (response) {
            if (response.code == 200) {
                $("#comment_section").hide();
            } else {
                if (response.code == 2003){
                    var isAccepted=confirm(response.message);
                    if(isAccepted){
                        //打开授权登录窗口
                        window.open("https://github.com/login/oauth/authorize?client_id=Iv1.125b50f58e0c91ac&redirect_uri=http://localhost:8080/callback&scope=user&state=1")
                        window.localStorage.setItem("closable",true);//登陆后关闭上面这个页面
                    }
                }else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}