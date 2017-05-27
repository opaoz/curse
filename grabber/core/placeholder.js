/**
 * App configuration placeholder.
 *
 * @access public
 * @namespace core\Placeholder
 *
 * @see <a href='https://www.npmjs.com/package/yamljs'>yamljs</a>
 * */

import YAML from 'yamljs';
import path from 'path';
import _ from 'underscore';

class Placeholder {
    /**
     * Initialize Placeholder object
     * @constructor
     * */
    constructor() {
        this.instance = YAML.load(path.join('./resources', 'application.yml'));
        this.stored = {};
    }

    /**
     * @params suffix string
     * @return  Object by suffix e.g 'server'
     * */
    getBySuffix(suffix) {
        return _.propertyOf(this.instance)(suffix) || {};
    }

    /**
     * @param {String} suffix string
     * @returns {Object}
     */
    getStoredBySuffix(suffix) {
        return _.propertyOf(this.stored)(suffix) || {};
    }

    /**
     * @param {Object} config
     */
    store(config) {
        this.stored = config;
    }
}

/**
 * @return Placeholder object
 * */
export default new Placeholder();
