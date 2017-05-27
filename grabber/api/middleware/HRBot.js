/**
 * @file HRBot
 *
 * @author opa_oz
 * @date 09/03/2017
 */

import {RtmClient, CLIENT_EVENTS, WebClient} from '@slack/client';
import {Promise} from 'bluebird';
import _ from 'underscore';

import placeholder from '../../core/placeholder';

const botConfig = placeholder.getBySuffix('slack');

const rtm = new RtmClient(botConfig.botToken);
const web = new WebClient(botConfig.userToken);

class HRBot {
    /**
     * @constructor
     */
    constructor() {
        this.connect();
    }

    /**
     * Connect to Slack channel
     */
    connect() {
        this.connection = new Promise((resolve, reject) => {
            rtm.on(CLIENT_EVENTS.RTM.AUTHENTICATED, (rtmStartData) => {
                console.info(`[Logged in as ${rtmStartData.self.name} of team ${rtmStartData.team.name}]`);
            });

            rtm.on(CLIENT_EVENTS.RTM.RTM_CONNECTION_OPENED, () => {
                return web.groups.list((err, info) => {
                    /* istanbul ignore next */
                    if (err) {
                        console.error(err);
                        return reject(err);
                    }

                    return _.each(info.groups, v => {
                        if (v.name === botConfig.botChannel) {
                            this.channel = v.id;
                            resolve();
                        }
                    });
                });
            });

            rtm.start();
        });
    }

    /**
     * Disconnect Slack
     * @returns {*|Socket|void}
     */
    disconnect() {
        return rtm.disconnect();
    }

    /**
     *
     * @param {String} message
     * @param {String|Boolean|Function} json
     * @param {Function} [callback]
     * @returns {Promise}
     */
    sendMessage/* istanbul ignore next */(message, json = true, callback = _.noop) {
        if (_.isFunction(json)) {
            callback = json;
        } else if (_.isString(json)) {
            message = `*${message}*\n` + '```' + json + '```';
        } else if (json) {
            message = '```' + message + '```';
        }

        return this.connection.then(() => {
            if (process.env.TEST_ENV) {
                return callback(null, true);
            }
            /* istanbul ignore next */
            return rtm.sendMessage(message, this.channel, callback);
        });
    }
}

export default new HRBot();
