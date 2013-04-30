/**
 * Class to represent common url operations
 * @param param
 * @constructor
 */
function Url(param) {
    if (typeof param === 'string') {
        this.parseUrl(param);
    }
    this.params = {};
}

Url.prototype.toString = function toString() {
    return this.getUrl();
};

Url.prototype.setParam = function setParam(name, value) {
    this.params[name] = value;
};

/**
 * Can take text or an object with key, values handle either a url
 * or values. will override baseUrl if already set
 * @param params
 */
Url.prototype.setParams = function setParams(params) {
    this.params = params;
};

/**
 * return final url with params included
 * Same output as toString()
 * @return {*}
 */
Url.prototype.getUrl = function getUrl() {
    return this.baseUrl + this.getParamString();
};

Url.prototype.getUrlEncoded = function getUrlEncoded(){
    return encodeURI(this.getUrl());
};

Url.prototype.getParamString = function getParamString() {
    var i = '',
        a = '',
        result = '';

    if(this.baseUrl.indexOf('?') == -1) {
    	result = '?';
    } else if (this.baseUrl.indexOf('?') < this.baseUrl.length - 1) {
    	a = '&';
    }
    
    var params = this.params;
    for (i in params) {
    	if(this.baseUrl.indexOf('?' + i + '=') == -1 
    			&& this.baseUrl.indexOf('&' + i + '=') == -1) {
    		result += a + i + "=" + params[i];
    		a = '&';
    	}
    }
    return result;
};

Url.prototype.setBaseUrl = function setBaseUrl(url) {
    this.baseUrl = url;
};

/**
 * Breaks up a url and overrides the baseUrl and params even if previously set
 * @param url
 * @return {*}
 */
Url.prototype.parseUrl = function parseUrl(url) { //replaceParams (url, replaceParams)
    var base, right, left, params,
        result, i, str;

    if (this.isSimpleUrl(url) === true) {
        this.setBaseUrl(url);
        return this.getUrl();
    }

    result = url.split('?');
    base = result[0];
    right = result[1];
    result = right.split('&');

    this.setBaseUrl(base);

    // Break apart URL into an array of parameters
    params = {};
    for (i in result) {
        str = result[i];
        left = str.substr(0,str.indexOf('=')); //using this instead of.split because some queries have more than one equals
        right = str.substr(str.indexOf('=')+1);
        params[left] = right;
    }

    this.setParams(params);
    return this.getUrl();
};

/**
 * A simple url is one that doesn't contain ?, &, or = anywhere
 * @param url
 * @return {Boolean}
 */
Url.prototype.isSimpleUrl = function isSimpleUrl(url){
    if (url.indexOf('?') === -1) {
        return true;
    }
    return false;
};