function Carousel(device) {
    this.listCarousel = {};
    this.device = device;
	this.horizontal='horizontal';
	this.vertical='vertical';
	this.carouselName = []
}

Carousel.deviceFactory = function deviceFactory(deviceType) {
    switch (deviceType) {
        case 'mobile':
            return new CarouselMobile();
        case 'tablet':
            return new CarouselTablet();
        default:
            return new CarouselMobile();
    }
};

Carousel.prototype.numberItemOnPortrait = function numberItemOnPortrait() {
	return this.device.numberItemOnPortrait();
};

Carousel.prototype.numberItemOnLanscape = function numberItemOnLanscape() {
	return this.device.numberItemOnLanscape();
};

Carousel.prototype.drawCarouselBanner = function drawCarouselBanner(params) {
	var elementName = params.elementName,
		mode = params.mode ? params.mode : this.horizontal,
		slidesPerSlide = params.slidesPerSlide ? params.slidesPerSlide: 1,
		loop = params.loop ? params.loop: true;
	
    carousel.listCarousel[elementName] = $(elementName).swiper({
		mode : mode, 
		slidesPerSlide : slidesPerSlide,
		loop:loop,
		autoPlay:2000
	});
	carousel.carouselName.push(elementName);
};

Carousel.prototype.drawCarousel = function drawCarousel(params) {
	var elementName = params.elementName,
		mode = params.mode ? params.mode : 'horizontal',
		slidesPerSlide = params.slidesPerSlide ? params.slidesPerSlide: 3,
		loop = params.loop ? params.loop: true;
		
	carousel.listCarousel[elementName] = $(elementName).swiper({
		mode : mode, 
		slidesPerSlide : slidesPerSlide,
		loop:loop,
	});
	carousel.carouselName.push(elementName);
	carousel.resizeFix();
};

Carousel.prototype.resizeFix = function resizeFix() {
	for(i in carousel.carouselName){
		try{
			carousel.listCarousel[carousel.carouselName[i]].resizeFix();
		}catch(err){
		}
	}
};