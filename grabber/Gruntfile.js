/*eslint-disable*/
'use strict';
/**
 * @file Gruntfile.js
 * serves as a main entry point for the application
 * @date 20.11.2015
 */

module.exports = function (grunt) {
    grunt.initConfig({
        concurrent: {
            dev: [
                'watch:js',
                'nodemon:dev'
            ],
            options: {
                logConcurrentOutput: true
            }
        },
        eslint: {
            target: ['Gruntfile.js', 'app.js', 'core/**/*.js', 'api/**/*.js', '!core/base64', '!node_modules', '!public']
        },
        jshint: {
            options: {
                jshintrc: '.jshintrc'
            },
            all: ['Gruntfile.js', 'app.js', 'core/**/*.js', 'api/**/*.js', '!core/base64/*.js', '!node_modules', '!public']
        },
        env: {
            dev: {
                NODE_ENV: 'development',
                SESSION_SECRET: 'keyboard cat'
            },
            prod: {
                NODE_ENV: 'production'
            }
        },
        nodemon: {
            dev: {
                script: './bin/www',
                options: {
                    cwd: __dirname,
                    ignore: ['node_modules/**', 'logs/**', '**/tests/**', 'public/**'],
                    ext: 'js'
                }
            }
        },
        apidoc: {
            myapp: {
                src: 'api/modules/',
                dest: 'public/apidoc/'
            }
        },
        mochaTest: {
            test: {
                options: {
                    reporter: 'mochawesome'
                },
                src: ['api/modules/**/tests/**/*.js']
            }
        },
        watch: {
            js: {
                files: ['**/**/*.js', '!node_modules/**', '!logs/**', '!**/tests/**', '!public/**'],
                tasks: ['jshint', 'eslint']
            }
        }
    });

    process.env.MOCHAWESOME_REPORTFILENAME = 'index';
    process.env.MOCHAWESOME_REPORTDIR = 'public/tests';

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-eslint');
    grunt.loadNpmTasks('grunt-env');
    grunt.loadNpmTasks('grunt-nodemon');
    grunt.loadNpmTasks('grunt-apidoc');
    grunt.loadNpmTasks('grunt-mocha-test');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-concurrent');

    grunt.registerTask('serve', [
        'jshint',
        'eslint',
        'env:dev',
        'apidoc',
        'concurrent'
    ]);

    grunt.registerTask('build', [
        'jshint',
        'eslint',
        'env:prod',
        'nodemon'
    ]);

    grunt.registerTask('test', ['mochaTest']);

}; // END of grunt configuration
