//@todo move "inherits" somewhere else
var inherits = function(childCtor, parentCtor) {
    function tempCtor() {};
    tempCtor.prototype = parentCtor.prototype;
    childCtor.superClass_ = parentCtor.prototype;
    childCtor.prototype = new tempCtor();
    childCtor.prototype.constructor = childCtor;
};

/**
 * Class to represent common url operations and set default params for common API urls
 * @param param
 * @constructor
 */
function ApiUrl(param) {
    Url.call(this);
    this.setDefaultParams();
	this.apiList = {
		'aSpot': settings.get('urls.apiBaseUrl') + 'webservice.php?controller=bestseller&type=bestsellerwomanday_product',
		'category': settings.get('urls.apiBaseUrl') + 'webservice.php?controller=category',
		'productInCategory': settings.get('urls.apiBaseUrl') + 'webservice.php?controller=product',/*&categoryID=59*/
		'productDetail': settings.get('urls.apiBaseUrl') + 'webservice.php?controller=product',/*&productID=270*/
		'shoppingCart': settings.get('urls.apiBaseUrl') + 'webservice.php?controller=shoppingcart&method=getBasket&customername=user',
	};
}
inherits(ApiUrl, Url);

ApiUrl.prototype.setDefaultParams = function setDefaultParams() {
	/*var ck = new Cookies();
	var dma = ck.get('dma')!=null ? ck.get('dma') : settings.get('general.dmaDefault');

	this.params['lang'] = (ck.get('lang') != null ? ck.get('lang') : settings.get('general.langDefault'));
	this.params['deviceType'] =  (ck.get('deviceType') != null ? ck.get('deviceType') : settings.get('general.deviceTypeDefault'));
	if(!this.params['dma']) {
		this.params['dma'] = dma;			
	}
	if(!this.params['maxResults']) {
		this.params['maxResults'] = settings.get('general.maxLoadedAPIResult');
	}*/
};

/**
 * Override method to append to params instead of wipe them all (which would destroy default params and make ApiUrl worthless)
 * @param params
 */
ApiUrl.prototype.setParams = function setParams(params) {
    for (var key in params) {
        this.params[key] = params[key];
    }
};
