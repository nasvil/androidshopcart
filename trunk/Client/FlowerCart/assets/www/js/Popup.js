function Popup() {
    this.type = {'TYPE_ALERT': 0, 'TYPE_CONFIRM': 1};
}

Popup.prototype.showMessage = function showMessage(title, message, jsCallBack, type) {
    if (platform == "Android") {
        AndroidFunction.showMessage(title, message, jsCallBack, type);
    	//alert(message);
    } else if (platform == "iPhone" || platform == "iPod" || platform == "iPad") {
        window.location = "js-call:showMessage('" + title + "',;,'" + message.replace(/'/g, "\\'") + "',;,'" + jsCallBack + "',;," + type + ")";
    } else {
        alert(message);
    }
};