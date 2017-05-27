/**
 * Application enumeration of constants.
 *
 * @access public
 * @namespace api\enumerations\Constants
 *
 * @see <a href='https://github.com/adrai/enum'>Node js enumeration</a>
 * */

import Enum from 'enum';

export default new Enum({

    /**
     * Api v1 namespace.
     * @type String
     * */
    API_ROUTE: '/api',

    /**
     * Month patterns for data parsing
     * @type Array
     */
    MONTHS: [
        {
            pattern: /январ/i,
            key: 0
        },
        {
            pattern: /феврал/i,
            key: 1
        },
        {
            pattern: /март/i,
            key: 2
        },
        {
            pattern: /апрел/i,
            key: 3
        },
        {
            pattern: /мая/i,
            key: 4
        },
        {
            pattern: /июн/i,
            key: 5
        },
        {
            pattern: /июл/i,
            key: 6
        },
        {
            pattern: /август/i,
            key: 7
        },
        {
            pattern: /сентябр/i,
            key: 8
        },
        {
            pattern: /октябр/i,
            key: 9
        },
        {
            pattern: /ноябр/i,
            key: 10
        },
        {
            pattern: /декабр/i,
            key: 11
        },
    ],

    /**
     * Socket error
     * @type String
     * */
    SOCKET_ERR: 'socket:error',

    /**
     * Socket sucess
     * @type String
     * */
    SOCKET_SUCCESS: 'socket:success',

    /**
     * Key for user saving for request
     * @type String
     * */
    AUTH_USER: 'user',

    /**
     * Validation rules error name.
     * @type String
     * */
    VALIDATION_ERROR: 'ValidationError',

    /**
     * Error code for resource duplicate.
     * @type Number
     * */
    MONGODB_DUPLICATE_ERROR_CODE: 11000,

    /**
     * Sort index for MongoDb
     * @type Number
     * */
    MONGODB_SORT_ASC: 1,

    /**
     * Desc sort index for MongoDb
     * @type Number
     * */
    MONGODB_SORT_DESC: -1
});
