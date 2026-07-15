
angular.module('myApp').directive('pressEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.pressEnter);
                });

                event.preventDefault();
            }
        });
    };
});



angular.module('myApp').directive('myDirective', function() {
    function link(scope, elem, attrs, ngModel) {
        ngModel.$parsers.push(function(viewValue) {
          var reg = /^[^`~!@#$%\^&*()+={}|[\]\\:';"<>?,./1-9]*$/;
          // if view values matches regexp, update model value
          if (viewValue.match(reg)) {
            return viewValue;
          }
          // keep the model value as it is
          var transformedValue = ngModel.$modelValue;
          ngModel.$setViewValue(transformedValue);
          ngModel.$render();
          return transformedValue;
        });
    }

    return {
        restrict: 'A',
        require: 'ngModel',
        link: link
    };      
});
