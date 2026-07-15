angular
    .module("myApp")
    .filter('offset', function() {
        return function(data, offset) {
            offset = parseInt(offset, 10);
          return data.slice(offset);
        };
    });