/**
 * App logging configuration.
 *
 * @access public
 * @namespace api\log4js
 *
 * @see <a href="https://github.com/nomiddlename/log4js-node">log4js-node</a>
 * */

import log4js from 'log4js';

/**
 * Custom logger
 * @params name
 * @return Logger
 * */
export default(name) => {
    log4js.configure(require('../../resources/log4js.json'), {cwd: './'});

    return log4js.getLogger(name);
};
