function CarouselMobile() {
    this.itemOnPortrait=3;
	this.itemOnLanscape=5;
}

CarouselMobile.prototype.loadDefault = function loadDefault() {
	
};

CarouselMobile.prototype.numberItemOnPortrait = function numberItemOnPortrait() {
	return this.itemOnPortrait;
};

CarouselMobile.prototype.numberItemOnLanscape = function numberItemOnLanscape() {
	return this.itemOnLanscape;
};