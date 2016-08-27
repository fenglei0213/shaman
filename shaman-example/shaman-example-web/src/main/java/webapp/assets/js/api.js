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

    var putData = {"rowkeyList": [{"rowkey": "testxxx", "value": "valuexxx"}]};
    // var data = {rowkeyList: [{rowkey: "testxxx", value: "valuexxx"}]};
    $.ajax({
        type: "POST",
        url: "http://cp01-tieba-data-1014.cp01.baidu.com:8080/fusion-api/api/putDsData.do",
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