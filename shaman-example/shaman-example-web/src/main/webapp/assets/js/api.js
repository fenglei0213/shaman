$(document).ready(function () {
    // var data = {rowkeyList: [{rowkey: "testxxx", value: "valuexxx"}]};
    // $.ajax({
    //     type: "POST",
    //     url: "http://cp01-tieba-data-1013.cp01.baidu.com:8080/fusion-api/job/doJoin.do",
    //     dataType: "json",
    //     contentType: "application/json",
    //     success: function (msg) {
    //         if (msg['status'] == 200) {
    //             console.log("success");
    //         } else {
    //             console.log("failed");
    //         }
    //     }
    // });


    $("#login").click(function () {
        var userName = $("#username").val();
        var passwd = $("#passwd").val();
        var putData = {"userName": userName, "passwd": passwd};
        // var data = {rowkeyList: [{rowkey: "testxxx", value: "valuexxx"}]};
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/shaman-example-web/user/login.do",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(putData),
            success: function (msg) {
                if (msg['status'] == 200) {
                    console.log("success");
                } else {
                    console.log("failed");
                }
            }
        });
    })

})