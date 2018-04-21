function requestData(url, successCallback, errorCallback) {
    $.ajax({
        url : url,
        contentType : "application/x-www-form-urlencoded; charset=utf-8",
        cache : false,
        success : function(data) {
            successCallback(data);
        }.bind(this),
        error : function(xhr, status, err) {
            errorCallback(xhr, status, err);
        }.bind(this)
    });
}

function requestDataByPost(url, data, successCallback, errorCallback) {
    $.ajax({
        type : 'POST',
        url : url,
        data : data,
        contentType : "application/x-www-form-urlencoded; charset=utf-8",
        cache : false,
        success : function(data) {
            successCallback(data);
        }.bind(this),
        error : function(xhr, status, err) {
            errorCallback(xhr, status, err);
        }.bind(this)
    });
}