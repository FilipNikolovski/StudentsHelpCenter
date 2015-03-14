// Karma configuration
// http://karma-runner.github.io/0.10/config/configuration-file.html

module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: '../../',

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            // bower:js
            'main/webapp/bower_components/modernizr/modernizr.js',
            'main/webapp/bower_components/jquery/dist/jquery.js',
            'main/webapp/bower_components/stomp-websocket/lib/stomp.min.js',
            'main/webapp/bower_components/sockjs-client/dist/sockjs.js',
            'main/webapp/bower_components/bootstrap/dist/js/bootstrap.js',
            'main/webapp/bower_components/json3/lib/json3.js',
            'main/webapp/bower_components/angular/angular.js',
            'main/webapp/bower_components/angular-ui-router/release/angular-ui-router.js',
            'main/webapp/bower_components/angular-resource/angular-resource.js',
            'main/webapp/bower_components/angular-cookies/angular-cookies.js',
            'main/webapp/bower_components/angular-sanitize/angular-sanitize.js',
            'main/webapp/bower_components/angular-translate/angular-translate.js',
            'main/webapp/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
            'main/webapp/bower_components/angular-translate-loader-partial/angular-translate-loader-partial.js',
            'main/webapp/bower_components/angular-dynamic-locale/src/tmhDynamicLocale.js',
            'main/webapp/bower_components/angular-local-storage/dist/angular-local-storage.js',
            'main/webapp/bower_components/angular-cache-buster/angular-cache-buster.js',
            'main/webapp/bower_components/angular-pagedown/angular-pagedown.js',
            'main/webapp/bower_components/ng-tags-input/ng-tags-input.min.js',
            'main/webapp/bower_components/blueimp-tmpl/js/tmpl.js',
            'main/webapp/bower_components/blueimp-load-image/js/load-image.js',
            'main/webapp/bower_components/blueimp-load-image/js/load-image-ios.js',
            'main/webapp/bower_components/blueimp-load-image/js/load-image-orientation.js',
            'main/webapp/bower_components/blueimp-load-image/js/load-image-meta.js',
            'main/webapp/bower_components/blueimp-load-image/js/load-image-exif.js',
            'main/webapp/bower_components/blueimp-load-image/js/load-image-exif-map.js',
            'main/webapp/bower_components/blueimp-canvas-to-blob/js/canvas-to-blob.js',
            'main/webapp/bower_components/jquery-file-upload/js/cors/jquery.postmessage-transport.js',
            'main/webapp/bower_components/jquery-file-upload/js/cors/jquery.xdr-transport.js',
            'main/webapp/bower_components/jquery-file-upload/js/vendor/jquery.ui.widget.js',
            'main/webapp/bower_components/jquery-file-upload/js/jquery.fileupload.js',
            'main/webapp/bower_components/jquery-file-upload/js/jquery.fileupload-process.js',
            'main/webapp/bower_components/jquery-file-upload/js/jquery.fileupload-validate.js',
            'main/webapp/bower_components/jquery-file-upload/js/jquery.fileupload-image.js',
            'main/webapp/bower_components/jquery-file-upload/js/jquery.fileupload-audio.js',
            'main/webapp/bower_components/jquery-file-upload/js/jquery.fileupload-video.js',
            'main/webapp/bower_components/jquery-file-upload/js/jquery.fileupload-ui.js',
            'main/webapp/bower_components/jquery-file-upload/js/jquery.fileupload-jquery-ui.js',
            'main/webapp/bower_components/jquery-file-upload/js/jquery.fileupload-angular.js',
            'main/webapp/bower_components/jquery-file-upload/js/jquery.iframe-transport.js',
            'main/webapp/bower_components/rangy/rangy-core.min.js',
            'main/webapp/bower_components/rangy/rangy-cssclassapplier.min.js',
            'main/webapp/bower_components/rangy/rangy-selectionsaverestore.min.js',
            'main/webapp/bower_components/rangy/rangy-serializer.min.js',
            'main/webapp/bower_components/textAngular/src/textAngular.js',
            'main/webapp/bower_components/textAngular/src/textAngular-sanitize.js',
            'main/webapp/bower_components/textAngular/src/textAngularSetup.js',
            'main/webapp/bower_components/angular-mocks/angular-mocks.js',
            // endbower
            'main/webapp/scripts/app/app.js',
            'main/webapp/scripts/app/**/*.js',
            'main/webapp/scripts/components/**/*.js',
            'test/javascript/**/!(karma.conf).js'
        ],


        // list of files / patterns to exclude
        exclude: [],

        // web server port
        port: 9876,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['PhantomJS'],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false
    });
};
