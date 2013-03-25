//using HTML5 localStorage instead of cookies

function LocalStores(){
}

LocalStores.prototype.get = function get(name){
    var str = localStorage.getItem(name);
    if (!str) {
        str = null;
    }
    return str;
};

LocalStores.prototype.set = function set(name, value, days){
    localStorage.setItem(name, value);
};

LocalStores.prototype.remove = function remove(name){
    localStorage.removeItem(name);
};

LocalStores.prototype.isAvailable = function isAvailable(value){
	return (typeof(value)!='undefined' && value!=null);
};

LocalStores.prototype.chooseValue = function chooseValue(){
	var ck = new Cookies();
	for(i in arguments){
		if(ck.isAvailable(arguments[i])){
			return arguments[i];
		}
	}
	return null;
};
