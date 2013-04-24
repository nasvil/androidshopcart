function Cart(){
	this.data = [];
	this.gift = {};
	this.deliveryType = -1;
	this.timeDelivery = -1;
	this.address = "";
	this.message = "";
}

Cart.prototype.add = function add(params){
	var item = params.item;
	console.log(item.ID);
	try{
		if(!cart.checkExit(item)){
			cart.data.push(item);
			popup.showMessage('','Add Success!!!','',popup.type.TYPE_ALERT);
		}
		
		cart.save();
		return true;
	}catch(err){
		console.log('err: '+err);
		return false;
	}
};

Cart.prototype.remove = function remove(params){
	var idProduct = params.idProduct;
	
	for(var i = 0; i< cart.data.length; i++){
		var item= cart.data[i];
		if(parseFloat(item.ID) == parseFloat(idProduct)){
			cart.data.splice(parseFloat(i),1);
			popup.showMessage('','Remove Success!!!','',popup.type.TYPE_ALERT);
		}
	}
	cart.save();
	cart.getFromLocal();
	utility.renderProductInCart();
};

Cart.prototype.checkExit = function checkExit(d){
	
	for(var i = 0; i< cart.data.length; i++){
		var item= cart.data[i];
		if(parseFloat(item.ID) == parseFloat(d.ID)){
			item.QUANTITY += 1;
			item.NAME = d.NAME;
			item.PRICE = d.PRICE;
			popup.showMessage('','Update Success!!!','',popup.type.TYPE_ALERT);
			return true;
		}
	}
	return false;
};

Cart.prototype.show = function show(params){
	
};

Cart.prototype.publish = function publish(params){
	
};

Cart.prototype.save = function save(){
	var w = {
		data: cart.data,
		gift: cart.gift,
		deliveryType: cart.deliveryType,
		timeDelivery: cart.timeDelivery,
		address: cart.address,
		message: cart.message
	};
	var d = JSON.stringify(w);
	utility.cookies.set('cart',d,365);
	
};

Cart.prototype.getFromLocal = function getFromLocal(){
	try{
		var d = JSON.parse(utility.cookies.get('cart'));
		cart.data = d.data;
		cart.gift = d.gift;
		cart.deliveryType = d.deliveryType;
		cart.timeDelivery = d.timeDelivery;
		cart.address = d.address;
		cart.message = d.message;
	}catch(err){
		utility.cookies.remove('cart');
	}
	
};