/**
 * BotController
 *
 * @access public
 * @namespace api\modules\v1\endpoint\controllers\BotController
 * */
import request from 'request';
import translit from 'translit';
import translitMap from 'translit-russian';
import _ from 'lodash';
import {map} from 'async';

const prefixUrl = 'http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryClass=place&QueryString=';

export default class BotController {

    constructor() {
        this.translit = translit(translitMap);
    }

    populate(array, cb = _.noop) {
        console.log('[Start populating]');

        return map(array, (v, done) => {
            console.log(`[Populating ${v} from DBPedia]`);

            return request({
                method: 'GET',
                url: `${prefixUrl}${this.translit(v)}`,
                json: true,
                headers: {
                    Accept: 'application/json'
                }
            }, (err, response, body) => {
                if (err) {
                    return done(err);
                }

                if (_.isEmpty(body) || _.isEmpty(body.results)) {
                    console.log(`[Empty results for ${v} (${this.translit(v)})]`);
                    return done(null);
                }

                const result = _
                        .chain(body.results)
                        .filter((v) => (v.description))
                        .first()
                        .value() || {};

                if (!_.isEmpty(result)) {
                    console.log(`[Got results for ${v} by "${this.translit(v)}" request]`);
                    return done(null, {name: v, value: result.description});
                }

                console.log(`[Empty results for ${v} (${this.translit(v)})]`);
                return done(null);
            });
        }, (err, result) => {
            if (err) {
                return cb(err);
            }

            if (_.isEmpty(result)) {
                return cb('Empty results');
            }

            console.log('[Finish populating]');
            return cb(null, _.compact(result));
        });
    }
}
