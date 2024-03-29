function Utility(device){
	this.device = device;
	this.cookies = new LocalStores();
	this.listDateTimePicker={};
}

Utility.deviceFactory = function deviceFactory(deviceType) {
	switch (deviceType) {
		case 'mobile':
			return new UtilityMobile();
		case 'tablet':
			return new UtilityTablet();
		default:
			return new UtilityMobile();
	}
};

/*******************************************************************************************************/

/*******************************************************************************************************/

Utility.prototype.getDeviceType = function getDeviceType(){
	switch (devTy) {
	    case 'mobile':
			utility.cookies.set("deviceType", "mobile", 365);
	    	break;
	    case 'tablet':
	    	utility.cookies.set("deviceType", "tablet", 365);
	    	break;
    	default:
    		utility.cookies.set("deviceType", "phone", 365);
	}
};

Utility.prototype.loadGeneralActions = function loadGeneralActions() {
	$('#category_page #btnMore').live("click",function(){
		utility.renderListProduct({
			idCategory: $(this).attr('idCategory'),
			isMoreClick: true
		});
	});
	
	$('#show-product-detail').live('click',function(){
		utility.hideFilterItem();
		utility.renderProductDetail({
			idProduct: $(this).attr('idProduct')
		});	
	});
	
	$('#more-like-this-product').live('click',function(){
		utility.renderProductMoreLikeThis({
			idProduct: $('#product_page').attr('idProduct'),
			isMoreClick: true
		});
	});
	
	$('#product_page .dwbw.dwb-s').live('click',function(){
		var item = utility.newItem($('#product_page').attr('idProduct'), $('#product_page').attr('productName'), $('#product_page').attr('price'))
			listGift = $('#product_page #group-gift input');
		cart.add({
			item : item
		});
		
		if(listGift && listGift !== undefined){
			console.log(listGift.length);
			for(var i = 0; i < listGift.length; i++){
				console.log('checked'+i+': '+listGift.eq(i).attr('checked'));
				if(listGift.eq(i).attr('checked') == "checked"){
					var k = utility.newItem(listGift.eq(i).attr('alt'),listGift.eq(i).parent().find('.ui-btn-text').html(),listGift.eq(i).attr('value'));
					cart.add({
						item : k
					});
				}
			}
		}
		
	});
	
	$('#product_page .dwbw.dwb-c').live('click',function(){
		console.log('linkShare: '+$('#product_page').attr('linkShare'));
		if (platform == "Android") {
			AndroidFunction.showShare($('#product_page').attr('linkShare'));
		}
	});
	
	$('#show-popup-info').live('click',function(){
		currentProductClick = $(this).parent();
		utility.showPopupDetail(currentProductClick);
		$('#trigger_click_item a').eq(1).click();
	});
	
	$('.dw-persp .dwo').live('click',function(){
		$("#time-delivery").mobiscroll('hide');
	});
	
	$('.category-item-title').live('click',function(){
		utility.renderListProduct({
			idCategory: $(this).attr('idCategory')
		});
	});

	$('#redirect_main').live('click',function(){
		utility.redirectMainPage();
	});
	
	$('#redirect_search').live('click',function(){
		utility.redirectSearchPage();
	});
	
	$('#redirect_cart').live('click',function(){
		utility.redirectCartPage();
	});
	
	$('.swiper-slide').live('click',function(){
		var idProduct = $(this).attr('idProduct');
		if(idProduct != undefined){
			utility.hideFilterItem();
			utility.renderProductDetail({
				idProduct: idProduct
			});
		}
	});
	
	$('#exit_app').click(function () {
        utility.hideExitAppButton();
    });

    $('#exit_button').click(function () {
        utility.hideExitAppButton();
        utility.exitAppAction();
    });
	
    $('#purchase_add').live('click',function(){
    	var item = utility.newItem($(currentProductClick).find('#show-product-detail').attr('idProduct'), $(currentProductClick).find('.heading-category').html(), $(currentProductClick).find('strong').eq(1).html());
		cart.add({
			item : item
		});
		$('#purchase_add').parent().find('a').eq(1).click();
    });
    
    $('#remove-product-from-cart').live('click',function(){
    	var idProduct = $(this).parent().parent().find('td').eq(0).find('a').attr('idProduct');
    	console.log($(this).parent().parent().find('td').find('a').eq(0));
    	console.log('idProduct: '+idProduct);
    	cart.remove({
			idProduct: idProduct
		});
    });
    
    $('#zoom_out').live('click',function(){
    	var i = $(this).parent().children().eq(0);
    	var w = $(i).find('img').eq(0).width() + 200;
    	console.log(w);
    	$(i).css('width',w);
    });
    
    $('#zoom_in').live('click',function(){
    	var i = $(this).parent().children().eq(0);
    	var w = $(i).find('img').eq(0).width() - 200;
    	console.log(w);
    	$(i).css('width',w);
    });
    
    $('#cart_page .android-ics').live('click',function(){
//    	utility.publishPayment();
    	$('#paypal_form_show input[type=submit]').click();
    });
    
};

Utility.prototype.getDeviceType = function getDeviceType() {
	switch (devTy) {
	    case 'mobile':
			utility.cookies.set("deviceType", "mobile", 365);
	    	break;
	    case 'tablet':
	    	utility.cookies.set("deviceType", "tablet", 365);
	    	break;
    	default:
    		utility.cookies.set("deviceType", "mobile", 365);
	}
};

Utility.prototype.encodeString = function encodeString(str) {
	return JSON.stringify(str);
};

Utility.prototype.decodeString = function decodeString(str) {
	return JSON.parse(str);
};

Utility.prototype.redirectMainPage = function redirectMainPage() {
	$('#trigger_click_item a').eq(3).click();
};

Utility.prototype.redirectSearchPage = function redirectSearchPage() {
	$('#trigger_click_item a').eq(5).click();
};

Utility.prototype.redirectCartPage = function redirectCartPage() {
	$('#trigger_click_item a').eq(4).click();
	utility.renderProductInCart();
};

Utility.prototype.activeNavBar = function activeNavBar(item) {
	$('.ui-footer').removeClass('ui-btn-active');
	$(item).addClass('ui-btn-active');
};

Utility.prototype.show = function show() {
	$('.background').removeClass('template');
	var h = $('.ui-page-active').outerHeight() + $('.ui-page-active .ui-header').outerHeight();
	$('.background').css('height',h);
	$.mobile.showPageLoadingMsg("a", "Loading ...");
};

Utility.prototype.hide = function hide() {
	$('.background').addClass('template');
	$.mobile.hidePageLoadingMsg();
};

Utility.prototype.realPrice = function realPrice(price) {
	try{
		if(price.length <= 3){
			return parseInt(price);
		}
		return parseInt(price.toString().substring(0,price.toString().indexOf('.')));
	}catch(err){
		return parseInt(price);
	}
};

Utility.prototype.createDateTimePicker = function createDateTimePicker(params) {
	var elementName = params.elementName,
		theme = params.theme ? params.theme : 'android-ics light',
		display = params.display ? params.display : 'top',
		mode = params.mode ? params.mode : 'mixed';
		
	$(elementName).mobiscroll('destroy');
	utility.listDateTimePicker[elementName] = $(elementName).mobiscroll().datetime({
        theme: theme,
        display: display,
        mode: mode
    },{minDate:new Date()});
};

Utility.prototype.hideFilterItem = function hideFilterItem() {
	$('#filter_contain').hide();
};

Utility.prototype.showFilterItem = function showFilterItem() {
	$('#filter_contain').show();
	$('#filter_contain').css('width',$('#my-wrapper').outerWidth());
	$('#my-wrapper form').appendTo($('#filter_contain'));
};

Utility.prototype.getCarouselItem = function getCarouselItem(item) {
	var template=$('#carousel_item').html();
	template=template.replace('[ID]',item.ID);
	template=template.replace('[ID]',item.ID);
	template=template.replace('[ID]',item.ID);
	template=template.replace('[NAME]',item.Name);
	return template;
};

Utility.prototype.getCarouselSlideItem = function getCarouselSlideItem(item) {
	var template=$('#carousel_slide_item').html();
	template=template.replace('[ID]',item.ID);
	template=template.replace('[IMG]',item.Image);
	template=template.replace('[TITLE]',item.Name);
	template=template.replace('[PRICE]',utility.realPrice(item.Price));
	return template;
};

Utility.prototype.renderArrayObject = function renderArrayObject(params,success,error) {
	dataHelper.getAPIDataBasic(params,success,error);
};

Utility.prototype.getItemInASpot = function getItemInCategory(item) {
	var template=$('#aspot_item').html();
	template=template.replace('[ID]',item.ID);
	template=template.replace('[IMG]',item.Image);
	template=template.replace('[TITLE]',item.Name);
	template=template.replace('[DES]',"");
	template=template.replace('[PRICE]',utility.realPrice(item.Price));
	return template;
};

Utility.prototype.renderASpot = function renderASpot(params,success,error) {
	$('.swiper-aspot .swiper-wrapper').html("");
	utility.hideFilterItem();
	utility.show();
	utility.renderArrayObject({
		url: 'http://flowercardvn.com/webservice/webservice.php?controller=bestseller&type=bestsellerwomanday_product',
	    isCachedLocal: false,
	    data: "",
	},function(result){
		for(i in result){
			$('.swiper-aspot .swiper-wrapper').append(utility.getItemInASpot(result[i]));
		}
		carousel.drawCarousel({
			elementName: '.swiper-aspot',
			mode: carousel.horizontal,
			slidesPerSlide: 1,
			loop: true
		});
		
		utility.hide();
	},function(req, status, e){
		
	});
};

Utility.prototype.renderListProductInCategoryToCarousel = function renderListProductInCategoryToCarousel(params) {
	var idCategory = params.idCategory,
		slidesPerSlide = params.slidesPerSlide ? params.slidesPerSlide : carousel.numberItemOnPortrait(),
		elementName = params.elementName,
		elementWrapper = params.elementWrapper;
	
	utility.show();
	api.setBaseUrl(api.apiList.productInCategory);
	utility.renderArrayObject({
		url: api.getUrl()+"&categoryID="+idCategory,
		isCachedLocal: false,
		data: "{idcategory:,"+idCategory+"}"
	},function(result){
		for(i in result){
			$(elementWrapper).append(utility.getCarouselSlideItem(result[i]));
		}
		var r = result.length;
		while(r <= slidesPerSlide && r > 0){
			$(elementWrapper).append(utility.getCarouselSlideItem(result[0]));
			r++;
		}
		carousel.drawCarousel({
			elementName: elementName,
			mode: carousel.horizontal,
			slidesPerSlide: slidesPerSlide,
			loop: true
		});
		utility.hide();
	},function(req, status, e){
		
	});
};

Utility.prototype.renderListCategoryToCarousel = function renderListCategoryToCarousel() {
	$('#main-list-category').html("");
	utility.hideFilterItem();
	utility.show();
	utility.renderArrayObject({
		url: 'http://flowercardvn.com/webservice/webservice.php?controller=category',
	    isCachedLocal: false,
	    data: "",
	},function(result){
		for(i in result){
			$('#main-list-category').append(utility.getCarouselItem(result[i]));
			utility.show();
			utility.renderListProductInCategoryToCarousel({
				idCategory: result[i].ID,
				slidesPerSlide: carousel.numberItemOnPortrait(),
				elementName: '#product_in_category_swiper_'+result[i].ID,
				elementWrapper: '#product_in_category_'+result[i].ID+' .swiper-wrapper'
			});
		}
		$('#main-list-category').listview('refresh');
	},function(req, status, e){
		
	});
};

Utility.prototype.getItemInCategory = function getItemInCategory(item) {
	var template=$('#category_item_template').html();
	template=template.replace('[ID]',item.ID);
	template=template.replace('[IMG]',item.Image);
	template=template.replace('[TITLE]',item.Name);
	template=template.replace('[DES]',"");
	template=template.replace('[PRICE]',utility.realPrice(item.Price));
	return template;
};

Utility.prototype.getMoreButtonInCategory = function getMoreButtonInCategory(params) {
	var template=$('#category_item_more_template').html();
	template = template.replace('[IDCATE]',params.idCategory);
	return template;
};

Utility.prototype.showPopupDetail = function showPopupDetail(li) {
	var a = $('#category_page #purchase'),
	idProduct = $(li).find('#show-product-detail').attr('idProduct');
	a.find('h3').html($(li).find('.heading-category').html());
	a.find('p').html($(li).find('.des-category').html());
	a.find('.price').html("$"+$(li).find('strong').eq(1).html());
};

Utility.prototype.renderListProduct = function renderListProduct(params) {
	var idCategory = params.idCategory,
		isMoreClick = params.isMoreClick ? params.isMoreClick : false;
	if(isMoreClick){
		$('#category_page #listview-ul li:last-child').remove();
	}else{
		$('#listview-ul').html("");
	}
	$('#category_page').attr('idCategory',idCategory);
	utility.show();
	utility.renderArrayObject({
		url: "http://flowercardvn.com/webservice/webservice.php?controller=product&categoryID=" + idCategory,
	    isCachedLocal: false,
	    data: "",
	},function(result){
		for(i in result){
			$('#listview-ul').append(utility.getItemInCategory(result[i]));
		}
		$('#listview-ul').append(utility.getMoreButtonInCategory({
			idCategory: idCategory
		}));
		if(!isMoreClick){
			$('#trigger_click_item a').eq(2).click();
		}
		$('#category_page #btnMore').button();
		$('#category_page #btnMore').parent().parent().css('visibility','hidden');
		$('#listview-ul').listview('refresh');
		// utility.hide();
	},function(req, status, e){
		
	});
};

Utility.prototype.getProductDetail = function getProductDetail(item) {
	var template=$('#product_metadata').html();
	template=template.replace('[IMG]',item.image);
	template=template.replace('[TITLE]',item.name);
	template=template.replace('[DESC]',item.description);
	template=template.replace('[PRICE]',utility.realPrice(item.price));
	
	return template;
};

Utility.prototype.getProductOptions = function getProductOptions(item) {
	var template=$('#product_option').html();
	template=template.replace('[ID]',item.id);
	template=template.replace('[SHARE]',item.share);
	return template;
};

Utility.prototype.getProductMoreLikeThis = function getProductMoreLikeThis(item) {
	var template=$('#product_more').html();
	template=template.replace('[IMG]',item.image);
	template=template.replace('[ID]',item.id);
	template=template.replace('[TITLE]',item.name);
	return template;
};

Utility.prototype.renderGiftProduct = function renderGiftProduct(params) {
	var data = params.gift.option,
		temp = $('#product_option_gift').html(),
		iTemp = "",
		items = "";
		
	if(data && data !== undefined){
		for(var i=0; i < data.length; i++){
			iTemp = $('#product_option_gift_item').html();
			iTemp = iTemp.replace('[ID]','item-'+i);
			iTemp = iTemp.replace('[ID]','item-'+i);
			iTemp = iTemp.replace('[ID]','item-'+i);
			iTemp = iTemp.replace('[PRICE]',(i*5));
			iTemp = iTemp.replace('[IDP]',(i*50));
			iTemp = iTemp.replace('[NAME]',data[i].name);
			items += iTemp;
		}
		temp = temp.replace('[GIFT_ITEM]',items);
		$('#product_detail').append(temp);
	}
};

Utility.prototype.renderProductDetail = function renderProductDetail(params) {
	var idProduct = params.idProduct;
	$('#product_detail').html("");
	utility.hideFilterItem();
	utility.show();
	utility.renderArrayObject({
		url: "http://flowercardvn.com/webservice/webservice.php?controller=detailProduct&productID="+idProduct,
	    isCachedLocal: false,
	    data: "",
	},function(result){
		$('*').scrollTop(0);
		console.log(result);
		$('#product_detail').append(utility.getProductDetail(result));
		utility.createItemPayment(result);
		
		$('#product_page').attr('idProduct',idProduct);
		$('#product_page').attr('price',utility.realPrice(result.price));
		$('#product_page').attr('linkShare',result.shareURL);
		$('#product_page').attr('productName',result.name);
		$('#product_page').attr('idCategory',result.idCategory);
		utility.renderGiftProduct({
			gift: result.option.Gift[0]
		});
		$('#product_detail').append(utility.getProductOptions(result));
		$('#trigger_click_item a').eq(0).click();
		$('#product_detail').listview('refresh');
		try{
			$("#product_page #group-gift").trigger("create");
		}catch(err){
			console.log(err);
		}
		
	},function(req, status, e){
		
	});
	utility.renderProductMoreLikeThis({
		idProduct: idProduct
	});
};

Utility.prototype.setPrositionListItemMore = function setPrositionListItemMore() {
	var wi = $('#item-wrapper').children().outerWidth(),
		wp = $('#item-wrapper').outerWidth(),
		ni = Math.floor(wp/wi),
		mg = (wp-wi*ni)/2 - 5;
	console.log('ni: '+ni+' -- wi: '+wi+' -- wp: '+wp+' -- mg: '+mg);
	 $('#item-wrapper').css('margin-left',mg);
};

Utility.prototype.renderProductMoreLikeThis = function renderProductMoreLikeThis(params) {
	var idProduct = params.idProduct,
		isMoreClick = params.isMoreClick ? params.isMoreClick : false;
	if(!isMoreClick){
		$('#product_detail_more #item-wrapper').html("");
	}
	utility.show();
	utility.renderArrayObject({
		url: "get-product-more",
	    isCachedLocal: false,
	    data: "",
		dataReturn:productListMore
	},function(result){
		for(i in result){
			$('#product_detail_more #item-wrapper').append(utility.getProductMoreLikeThis(result[i]));
		}
		utility.setPrositionListItemMore();
		// utility.hide();
	},function(req, status, e){
		
	});
	
};

Utility.prototype.newItem = function newItem(id,name,price) {
	var item = {
		ID: 0,
		NAME: "",
		PRICE: 0,
		QUANTITY: 0
	};
	
	item.ID = id;
	item.NAME = name;
	item.PRICE = price;
//	item.ID = $('#product_page').attr('idProduct');
//	item.NAME = $('#product_page').attr('productName');
//	item.PRICE = $('#product_page').attr('price');
	item.QUANTITY = 1;
	return item;
};

Utility.prototype.addToCart = function addToCart(params) {
	var idProduct = params.idProduct,
		quantity = 1
		price = params.price ? params.price : 0;
	
};

Utility.prototype.getProductInCart = function getProductInCart(item) {
	var template = '<tr>'+
						'<td><a id="show-product-detail" href="#" idProduct="[ID]">[NAME]</a></td>'+
						'<td>[QUANTITY]</td>'+
						'<td>$[PRICE]</td>'+
						'<td><a class="delete-item-form-cart" data-icon="delete" data-role="button" data-iconpos="notext" data-inline="true" id="remove-product-from-cart" href="#">Remove</a></td>'+
					'</tr>';
	template=template.replace('[ID]',item.ID);
	template=template.replace('[NAME]',item.NAME);
	template=template.replace('[QUANTITY]',item.QUANTITY);
	template=template.replace('[PRICE]',item.PRICE);
	return template;
};

Utility.prototype.renderProductInCart = function renderProductInCart() {
	$('#cart_page #table-custom tbody').html("");
	for(var i = 0; i<cart.data.length; i++ ){
		$('#cart_page #table-custom tbody').append(utility.getProductInCart(cart.data[i]));
	}
	$('#table-custom').table('refresh');
	$('.delete-item-form-cart').button()
	$('#table-custom').parent().children('a').css('display','none');
};


Utility.prototype.showExitAppButton = function showExitAppButton(){
	$('#exit_app').removeClass('template');
};

Utility.prototype.exitAppAction = function exitAppAction(){
	if(platform == "Android"){
		AndroidFunction.exitApp();
	}
};

Utility.prototype.hideExitAppButton = function hideExitAppButton(){
	$('#exit_app').addClass('template');
};

Utility.prototype.releaseFocus = function releaseFocus(){
	$('input').focusout();
};


Utility.prototype.publishPayment = function publishPayment() {
	miniCartId + '=reset';
	utility.createPayment();
};

Utility.prototype.createPayment = function createPayment() {
	for( var i = 0 ; i < cart.data.length ; i++ ){
		$('#paypal_checkout_form').append(utility.createItemPayment(cart.data[i]));
		$('#paypal_checkout_form form').eq(i).submit();
	}
};

Utility.prototype.createItemPayment = function createItemPayment(item) {
	
	$('#paypal_checkout_form input[name=item_name]').val(item.name);
	$('#paypal_checkout_form input[name=business]').val(settings.get('paypals.account'));
	$('#paypal_checkout_form input[name=quantity]').val(1);
	$('#paypal_checkout_form input[name=amount]').val(utility.realPrice(item.price));
};

Utility.prototype.showExternalLink = function showExternalLink(link) {
	console.log('showExternalLink: '+link);
	$('.fancybox').attr('href',link);
	$('.fancybox').click();
};


