function Cookies(){
}

Cookies.prototype.get = function get(name){
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0){ 
			var cookie = c.substring(nameEQ.length,c.length);
			if(cookie!=null){
				cookie = cookie.split(/\~|\%7E/);
				return cookie[cookie.length-1];
			}
			return cookie;
		}
    }
    return null;
};

Cookies.prototype.set = function set(name, value, days){
	var expires = "", date = new Date();
    if (days) {
        date.setTime(date.getTime()+(days*24*60*60*1000));
        expires = "; expires="+date.toGMTString();
    }
    document.cookie = name+"="+value+expires+"; path=/";
};

Cookies.prototype.remove = function remove(name){
	this.set(name,"",-1);
};

Cookies.prototype.isAvailable = function isAvailable(value){
	return (typeof(value)!='undefined' && value!=null);
};

Cookies.prototype.chooseValue = function chooseValue(){
	var ck = new Cookies();
	for(i in arguments){
		if(ck.isAvailable(arguments[i])){
			return arguments[i];
		}
	}
	return null;
};