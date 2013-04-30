/**
 * Accessing config settings in JavaScript
 * @param json
 * @constructor
 */
function Settings(json) {
    this.map = json;
}

/**
 * @public
 */
Settings.prototype.set = function set() {
};

/**
 * @public
 */
Settings.prototype.get = function get(name) {
    return this.map[name];
};

