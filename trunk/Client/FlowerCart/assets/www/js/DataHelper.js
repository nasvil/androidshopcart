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
            success: function (json) {
			json = '[{"ID":"73","Name":"M\u1eebng sinh em b\u00e9"},{"ID":"59","Name":"Congratulatory Flowers"},{"ID":"46","Name":"Flower Of Love"},{"ID":"69","Name":"Mother\'s Day"},{"ID":"45","Name":"Condolence Flowers"},{"ID":"60","Name":"Office Flowers"},{"ID":"74","Name":"B\u00e1nh kem tr\u00e1i c\u00e2y t\u01b0\u01a1i"},{"ID":"68","Name":"Qu\u00e0 t\u1eb7ng k\u00e8m"},{"ID":"71","Name":"Just because"},{"ID":"72","Name":"Birthday"},{"ID":"70","Name":"Woman\'s Day"}]';
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
