angular.module('studentshelpcenterApp')
    .directive('dropzone', function () {
        return function (scope, element, attrs) {
            var config, dropzone;

            config = scope[attrs.dropzone];
            dropzone = new Dropzone(element[0], config.options);

            angular.forEach(config.eventHandlers, function (handler, event) {
                dropzone.on(event, handler);
            });
        };
    });
