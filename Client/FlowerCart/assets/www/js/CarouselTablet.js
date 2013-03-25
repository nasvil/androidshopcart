function CarouselTablet() {
	this.itemOnPortrait=5;
	this.itemOnLanscape=7;
}

CarouselTablet.prototype.loadDefault = function loadDefault() {
	
};

CarouselTablet.prototype.numberItemOnPortrait = function numberItemOnPortrait() {
	return this.itemOnPortrait;
};

CarouselTablet.prototype.numberItemOnLanscape = function numberItemOnLanscape() {
	return this.itemOnLanscape;
};