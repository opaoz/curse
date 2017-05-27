/**
 * @file router
 *
 * @author opa_oz
 * @date 10/03/2017
 */
import request from 'request';
import _ from 'lodash';
import {CronJob} from 'cron';

import BotController from '../controllers/BotController';

const prefix = 'http://www.semanticweb.org/анна/ontologies/2016/4/untitled-ontology-41#';

export default class Router {
    /**
     * @constructor
     */
    constructor() {
        this.ctrl = new BotController();
        this.job = new CronJob({
            cronTime: '00 30 11 * * 1-5',
            start: false,
            timeZone: 'Europe/Moscow',
            onTick: this.onTick.bind(this)
        });

        this.job.start();
        this.onTick();
    }

    onTick() {
        return this.getSettlements((err, array) => {
            if (err) {
                return console.log('[Job doesnt finish]');
            }

            this.ctrl.populate(array, (err, array) => {
                let result = {};
                _.each(array, (v) => {
                    result[`${prefix}${v.name}`] = `${Date.now()}|${v.value.replace('|', ' ')}`;
                });
                result = JSON.stringify(result);
                const length = result.length;

                console.log('[Sending result to server]');

                return request({
                    method: 'POST',
                    url: 'http://127.0.0.1:8080',
                    body: result,
                    headers: {
                        'Content-Length': length
                    }
                }, (err, response, body) => {
                    if (err) {
                        return console.error(err);
                    }

                    console.log('[Jobs done]');
                    console.log(body);
                });
            });
        });
    }

    getSettlements(cb = _.noop) {
        return request({
            method: 'POST',
            url: 'http://127.0.0.1:8080',
            body: {settlements: true},
            json: true,
            headers: {
                'Content-Length': 20
            }
        }, (err, response, body) => {
            if (err) {
                console.error(err);
                return cb(err);
            }

            if (_.isEmpty(body)) {
                return cb('[Empty body]');
            }

            body = _
                .chain(body)
                .map(_.property('settlement'))
                .map((v) => (v.replace(prefix, '')))
                .filter((v) => (!(/[0-9]+/gi.test(v))))
                .value();

            console.log(`[Got ${body.length} settlements]`);

            return cb(null, body);
        });
    }
}
