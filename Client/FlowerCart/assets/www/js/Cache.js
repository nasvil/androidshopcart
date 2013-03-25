var cacheData = [];

function Cache(){}

/**
* Set data to Cache with the key is a specified url
* params
* url : specified url and its query string
* data : data taken from the API
*/
Cache.prototype.set = function set(url, data){
	cacheData.push({url: url, data: data});
}

/**
* Get data from Cache by a specified url
* params
* url : specified url and its query string
* return : data
*/
Cache.prototype.get = function get(url){
	var data = null;
	for(var i in cacheData){
		if(cacheData[i].url === url){
			data = cacheData[i].data;
			break;
		}
	}
	return data;
}