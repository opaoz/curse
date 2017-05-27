/*eslint-disable*/
'use strict';

/**
 * Created by opa_oz on 05.07.16.
 */
require('babel-register');
var should = require('should');
var HRBot = require('../../../../middleware/HRBot').default;

describe("HR Slack Bot", function () {
    this.timeout(150000000);

    it('should send test message', function (done) {
        HRBot.sendMessage('test', function (err, result) {
            should(err).be.empty();
            should(result).be.true; // jshint ignore:line
            done();
        });
    });

    it('should send test json message', function (done) {
        HRBot.sendMessage('{\"json\":true}', true, function (err, result) {
            should(err).be.empty();
            should(result).be.true; // jshint ignore:line
            done();
        });
    });

    it('should send test message with json', function (done) {
        HRBot.sendMessage('test', '{\"json\":true}', function (err, result) {
            should(err).be.empty();
            should(result).be.true; // jshint ignore:line
            done();
        });
    });

    after(function () {
        HRBot.disconnect();
    });
});