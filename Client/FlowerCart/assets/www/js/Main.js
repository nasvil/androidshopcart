var PAGE = {
		INDEX:0,
		CATEGORY:1,
		PRODUCT:2,
		SEARCH:3,
		CONTACT:4
	},
	popup,
	utility, 
	dataHelper,
	carousel,
	cache,
	devTy="mobile",
	cart,
	currentProductClick,
	platform,
	settings,
	api;

//config jquery mobile
$(document).bind("mobileinit", function(){
	$.mobile.loadingMessageTextVisible = true;
	$.mobile.allowCrossDomainPages=true;
	$.support.cors=true;
	defaultDialogTransition="slidefade";
	defaultPageTransition="slidefade";
	loadingMessage="Loading";
	loadingMessageTextVisible=true;
	loadingMessageTheme="a";
	pageLoadErrorMessage="Error Loading Page";
	pageLoadErrorMessageTheme="a";
});

//call js when dom html load success not use $(document).ready() for jquery mobile
$(document).bind('pageinit',function(){
	console.log('pageinit');
	
});
var categoryList = [{"id": "1","name": "Hoa tình yêu"},
					{"id": "2","name": "Ngày của mẹ"},
					{"id": "3","name": "Quốc tế phụ nữ"}];
					
var productListBest = [{"id": "1","name": "Hoa lan","image":"img/slider/1.jpg","price":"20"},
						{"id": "2","name": "Hoa hồng","image":"img/slider/1.jpg","price":"20"},
						{"id": "3","name": "Hoa ly ly","image":"img/slider/1.jpg","price":"20"}];
						
var productList = [{"id":"1","name":"Birthday Confetti","image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"2","name":"Chantilly Pink Roses", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"3","name":"Coral Sunset ", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"4","name":"Birthday Confetti","image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"5","name":"Chantilly Pink Roses", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"6","name":"Coral Sunset ", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"7","name":"Birthday Confetti","image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"8","name":"Chantilly Pink Roses", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"9","name":"Coral Sunset ", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"10","name":"Birthday Confetti","image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"11","name":"Chantilly Pink Roses", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"12","name":"Coral Sunset ", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"}];
					
var productListMore = [{"id":"13","name":"Birthday Confetti","image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"14","name":"Chantilly Pink Roses", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"15","name":"Coral Sunset ", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"16","name":"Birthday Confetti","image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"17","name":"Chantilly Pink Roses", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"18","name":"Coral Sunset ", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"19","name":"Birthday Confetti","image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"20","name":"Chantilly Pink Roses", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"21","name":"Coral Sunset ", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"22","name":"Birthday Confetti","image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"23","name":"Chantilly Pink Roses", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"},
					{"id":"24","name":"Coral Sunset ", "image":"img/slider/1.jpg","shortDescription":"Môtả","price":"20"}];
					
var productDetail = [{"idCategory":"1","id":"1","price":"20","name":"Birthday Confetti","image":"img/slider/1.jpg",
"Description":"Môtả","option":{"Gift":{"name":"quà tặng kèm","type" : "image","option": {"uncheck":"img/slider/1.jpg", "card":"img/slider/1.jpg","chocolate":"img/slider/1.jpg", "bear":"img/slider/1.jpg"}},"Delivery":{"name":"chon cách giao hàng","type" : "image","option":{"normal":"giao hang thong thuong","exactlyday":"giao dung ngay","exactlyhour":"giao dung gio"}},"TimeDelivery":{"name":"thời điểm giao hàng","type":"datetimepicker"},"AddressAndMesgage":{"name":"Địađiểm và lời nhắn","type":"textfield"}}}];
					
$(document).ready(function(){
	console.log('$(document).ready');
	
	// init param
	/*********************************************/
	settings = new Settings({
		"urls.postercdnurl": "http://flowercardvn.com/image/cache/",
		"urls.publicBaseUrl": "http://flowercardvn.com/",
		"urls.proxyBaseUrl": "http://flowercardvn.com/",
		"urls.apiBaseUrl": "http://flowercardvn.com/webservice/",
		"urls.signupUrl": "",
	});
	platform = "Android";
	popup=new Popup();
	api = new ApiUrl();
	utility=new Utility(Utility.deviceFactory(devTy));
	utility.getDeviceType();
	dataHelper=new DataHelper();
	carousel=new Carousel(Carousel.deviceFactory(devTy));
	cache=new Cache();
	cart= new Cart();
	cart.getFromLocal();
	
	//
	utility.loadGeneralActions();
	utility.createDateTimePicker({
		elementName:'#cart_page #time-delivery'
	});
	utility.hideFilterItem();
	utility.renderASpot();
	utility.renderListCategoryToCarousel();
});

$('#main_page').live('pagehide', function () {
    // utility.hideFilterItem();
});

$('#category_page').live('pagehide', function () {
	utility.hideFilterItem();
});

$('#category_page').live('pageshow', function () {
	console.log('category_page');
	try{
		utility.showFilterItem();
		var idCategory = $('#category_page').attr('idCategory');
		if(isNaN(idCategory)){
			var idCategory_product = $('#product_page').attr('idCategory');
			if(isNaN(idCategory_product)){
				utility.renderListProduct({
					idCategory: idCategory_product
				});
			}else{
				window.location = $('base').attr('href');
			}
		}else{
			utility.renderListProduct({
				idCategory: idCategory
			});
		}	
	}catch(err){
		window.location = $('base').attr('href');
	}
});

$('#product_page').live('pageshow', function () {
	console.log('product_page');
	try{
		var idProduct = $('#product_page').attr('idProduct');
		var idCategory = $('#product_page').attr('idCategory');
		if(isNaN(idProduct)){
			if(isNaN(idCategory)){
				utility.renderListProduct({
					idCategory: idCategory
				});
			}else{
				window.location = $('base').attr('href');
			}
		}else{
			utility.renderProductDetail({
				idProduct: idProduct
			});
		}
	}catch(err){
		window.location = $('base').attr('href');
	}
});

$('#cart_page').live('pageshow', function () {
	// utility.hideFilterItem();
	utility.renderProductInCart();
	console.log('cart_page');
});

$('#main_page').live('pageshow', function () {
	// utility.hideFilterItem();
	console.log('main_page');
	try{
		carousel.resizeFix();
	}catch(err){
		
	}
});

function testGet()
{
    // getCORS('http://ucommbieber.unl.edu/CORS/cors.php', null, function(data){alert(data);});
	getCORS('http://demo.ganeshbabujayaraman.com/capitals-json-cors.php', null, function(data){alert(data);});
}
