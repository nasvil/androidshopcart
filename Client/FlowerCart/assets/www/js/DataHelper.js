function DataHelper(){
	this.GET = 'GET';
    this.POST = 'POST';
	this.xhr=new Xhr();
}

DataHelper.prototype.getAPIDataBasic = function getAPIDataBasic(params,success,error) {
    var result = {},
    	url = params.url,
	    isCachedLocal = params.isCachedLocal === true ? true : false,
	    data = params.data ? params.data : '',
	    method = params.method === undefined ? this.GET : params.method;
	
    if (cache.get(url) === null) {
        this.xhr.get({
            url: url,
            data: data,
            type: method,
            dataType: 'json',
			dataReturn: params.dataReturn,
            success: function (json) {
                if (typeof json === 'string') {
                    result = JSON.parse(json);
                } else {
                    result = json;
                }
				if(isCachedLocal){
					cache.set(params.url, result);
				}
                if (success !== undefined) {
                    success(result);
                }
            },
            error: function (xhr, status, e) {
                if (error !== undefined) {
                    error(xhr, status, e);
                }
            }
        });
    } else if (params.success !== undefined) {
        if (async) {
            setTimeout(function () {
                params.success(cache.get(url));
            }, 100);
        } else {
            params.success(cache.get(url));
        }
    }
};
