/**
 * Wrap Xhr requests, adding the ability to group and cancel Xhr requests to prevent callbacks from firing
 * when clicking away from a page with outstanding requests
 * Supported callbacks:
 *     success
 *     error
 *
 * Example 1 - simple post
 * var xhr = new Xhr();
 * xhr.post({url:url, data:data});
 *
 * Example 2 - Xhr grouping
 * var xhrGroup1 = new Xhr();
 * var xhrGroup2 = new Xhr();
 * xhrGroup1.get({url:url});
 * xhrGroup1.cancel();
 * xhrGroup2.get({url:url});
 *
 * @param logger = Instance of Logger
 * @constructor
 */
var Xhr = function Xhr() {
    this.cancelled = false;
    //this.requests = [];
};

/**
 * @public
 * @param params
 */
Xhr.prototype.get = function get(params) {
    params.type = 'GET';
    this.send(params);
};

/**
 * @public
 * @param params
 */
Xhr.prototype.post = function post(params) {
    params.type = 'POST';
    this.send(params);
};

/**
 * @private
 * @param params
 * @return {*}
 */
Xhr.prototype.send = function get2(params) {
    var type = params['type'],
        url = params['url'],
        data = params['data'],
        success = params['success'],
        error = params['error'],
        dataType,
        acceptHeader,
        timeout,
        async,
        cache,
        headers = {};
		
	/*****/
	console.log('xhr -- url: '+url);
	if(params.dataReturn){
		success(params.dataReturn);
		return;
	}
	/*****/
	
    if (params['dataType']) {
        dataType = params['dataType'];
    } else {
        dataType = 'json';
    }
    if (dataType === 'xml') {
        acceptHeader = 'application/xml';
    } else if (dataType === 'json') {
        acceptHeader = 'application/json';
    } else {
        acceptHeader = '*/*';
    }
    if (params['timeout']) {
        timeout = params['timeout'];
    } else {
        timeout = '30000';
    }
    if (params['async']) {
        async = params['async'];
    } else {
        async = true;
    }
    if (params['cache'] === undefined) {
        cache = true;
    } else {
        cache = params['cache'];
    }
    if (params['headers']) {
    	headers = params['headers'];
    }
    if(!headers['Accept']) {
    	headers['Accept'] = acceptHeader;
    }
    this.cancelled = false;
   
	getCORS(url, null, function(data){
		success(data);
	});
    /*$.ajax({
        url: 'http://flowercardvn.com/webservice/webservice.php?controller=category',
        data: data,
        async: async,
        cache: cache,
        type: "GET",
        dataType: 'json',
        timeout: timeout,
		headers: headers,
        success: function (data) {
            // if (this.cancelled === true) {
            // } else {
				console.log(data);
                success(data);
            // }
        },
        error: function (req, status, e) {
			// if(error) {
				error(req, status, e);
			// }
            // if (this.cancelled === true) {
            // } else {
                // if (this.isTimedOut(status, e) === true) {
                    // return;
                // }
            // }
        }
    });*/
};

/**
 * @private
 * @param status
 * @param e
 * @return {Boolean}
 */
Xhr.prototype.isTimedOut = function isTimedOut(status, e) {
    if (status === 'timeout') {
        return true;
    }
    return e.toString().indexOf("NETWORK_ERR") !== -1;
};

/**
 * @public
 * Cancels all outstanding requests
 */
Xhr.prototype.cancel = function cancel() {
    ////console.log('[XHR] Cancelling Xhr requests..');
    this.cancelled = true;
};